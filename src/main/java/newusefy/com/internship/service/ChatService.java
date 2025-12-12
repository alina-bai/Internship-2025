package newusefy.com.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import newusefy.com.internship.dto.*;
import newusefy.com.internship.entity.ChatMessage;
import newusefy.com.internship.entity.ChatSession;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.ChatMessageRepository;
import newusefy.com.internship.repository.ChatSessionRepository;
import newusefy.com.internship.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public ChatService(RestTemplate restTemplate,
                       ChatSessionRepository chatSessionRepository,
                       ChatMessageRepository chatMessageRepository,
                       UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    // ======================================================
    // 1. ОБРАБОТКА ЧАТА
    // ======================================================

    @Transactional
    public ChatResponseDto handleChat(ChatRequestDto request, Principal principal) {

        String username = principal.getName();              // ← берем username
        User user = userRepository.findByUsername(username) // ← ищем User в БД
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        ChatSession session;

        if (request.getChatSessionId() == null) {
            // новая сессия
            session = createNewSession(request.getPrompt(), user);
        } else {
            // продолжаем существующую
            session = validateSessionOwner(request.getChatSessionId(), user);
        }

        // сохраняем сообщение пользователя
        saveMessage(session, "user", request.getPrompt());

        // получаем ответ AI
        String aiText = generateAnswer(request.getPrompt());

        // сохраняем сообщение ИИ
        saveMessage(session, "ai", aiText);

        // обновляем updatedAt
        session.setUpdatedAt(LocalDateTime.now());
        chatSessionRepository.save(session);

        return new ChatResponseDto(aiText, session.getId());
    }

    // ======================================================
    // 2. ИСТОРИЯ СЕССИЙ
    // ======================================================

    public List<ChatSessionSummaryDto> getUserSessions(Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        var sessions = chatSessionRepository.findByUserIdOrderByUpdatedAtDesc(user.getId());

        List<ChatSessionSummaryDto> result = new ArrayList<>();

        for (ChatSession s : sessions) {
            result.add(new ChatSessionSummaryDto(
                    s.getId(),
                    s.getTitle(),
                    s.getUpdatedAt()
            ));
        }

        return result;
    }

    public List<ChatMessageDto> getSessionMessages(Long sessionId, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        ChatSession session = validateSessionOwner(sessionId, user);

        var msgs = chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);

        List<ChatMessageDto> dtos = new ArrayList<>();
        for (ChatMessage m : msgs) {
            dtos.add(new ChatMessageDto(
                    m.getId(),
                    m.getRole(),
                    m.getContent(),
                    m.getCreatedAt()
            ));
        }

        return dtos;
    }

    // ======================================================
    // 3. ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ======================================================

    private ChatSession validateSessionOwner(Long sessionId, User user) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return session;
    }

    private ChatSession createNewSession(String prompt, User user) {
        ChatSession session = new ChatSession();
        session.setUser(user);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        session.setTitle(generateTitle(prompt));

        return chatSessionRepository.save(session);
    }

    private void saveMessage(ChatSession session, String role, String content) {
        ChatMessage msg = new ChatMessage();
        msg.setSession(session);
        msg.setRole(role);
        msg.setContent(content);
        msg.setCreatedAt(LocalDateTime.now());
        chatMessageRepository.save(msg);
    }

    private String generateTitle(String prompt) {
        if (prompt == null || prompt.isBlank()) return "New chat";
        return prompt.length() > 40 ? prompt.substring(0, 40) + "..." : prompt;
    }

    // ======================================================
    // 4. G E M I N I  API
    // ======================================================

    public String generateAnswer(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            return "Сообщение не может быть пустым.";
        }

        try {
            String finalUrl = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = """
            {
              "contents": [{
                "parts": [{ "text": "%s" }]
              }]
            }
            """.formatted(prompt.replace("\"", "\\\""));

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.exchange(finalUrl, HttpMethod.POST, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode textNode =
                    root.path("candidates").get(0)
                            .path("content")
                            .path("parts").get(0)
                            .path("text");

            if (textNode.isTextual()) return textNode.asText();

            return "⚠️ AI ответ не распознан";

        } catch (Exception e) {
            return "Ошибка AI: " + e.getMessage();
        }
    }
}
