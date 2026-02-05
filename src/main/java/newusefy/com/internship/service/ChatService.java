package newusefy.com.internship.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import newusefy.com.internship.dto.ChatRequestDto;
import newusefy.com.internship.dto.ChatResponseDto;
import newusefy.com.internship.dto.ChatMessageDto;
import newusefy.com.internship.entity.ChatMessage;
import newusefy.com.internship.entity.ChatSession;
import newusefy.com.internship.entity.Section;
import newusefy.com.internship.model.User;
import newusefy.com.internship.repository.ChatMessageRepository;
import newusefy.com.internship.repository.ChatSessionRepository;
import newusefy.com.internship.repository.SectionRepository;
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
    private  SectionRepository sectionRepository = null;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public ChatService(
            RestTemplate restTemplate,
            ChatSessionRepository chatSessionRepository,
            ChatMessageRepository chatMessageRepository,
            UserRepository userRepository
    ) {
        this.restTemplate = restTemplate;
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
    }

    // ======================================================
    // 1. ОСНОВНОЙ ЧАТ (Week 13 logic)
    // ======================================================

    @Transactional
    public ChatResponseDto handleChat(ChatRequestDto request, Principal principal) {

        User user = getUser(principal);

        ChatSession session;

        // 👉 если пользователь выбрал конкретный tab
        if (request.getChatSessionId() != null) {
            session = validateSessionOwner(request.getChatSessionId(), user);
        } else {
            // 👉 иначе ищем активный чат в section
            session = chatSessionRepository
                    .findByUserIdAndSectionIdAndActiveTrue(
                            user.getId(),
                            request.getSectionId()
                    )
                    .orElseGet(() ->
                            createNewSectionSession(user, request.getSectionId())
                    );
        }

        // сохраняем вопрос пользователя
        saveMessage(session, "user", request.getPrompt());

        // формируем prompt с контентом лекции
        String fullPrompt = buildPromptWithSection(
                request.getSectionId(),
                request.getPrompt()
        );

        // ответ AI
        String aiAnswer = generateAnswer(fullPrompt);

        // сохраняем ответ AI
        saveMessage(session, "ai", aiAnswer);

        session.setUpdatedAt(LocalDateTime.now());
        chatSessionRepository.save(session);

        return new ChatResponseDto(aiAnswer, session.getId());
    }

    // ======================================================
    // 2. СОЗДАНИЕ НОВОГО TAB (НОВЫЙ ЧАТ В SECTION)
    // ======================================================

    @Transactional
    public Long startNewChat(Long sectionId, Principal principal) {

        User user = getUser(principal);

        // деактивируем текущий активный чат
        chatSessionRepository
                .findByUserIdAndSectionIdAndActiveTrue(user.getId(), sectionId)
                .ifPresent(session -> {
                    session.setActive(false);
                    chatSessionRepository.save(session);
                });

        ChatSession newSession = createNewSectionSession(user, sectionId);
        return newSession.getId();
    }

    // ======================================================
    // 3. ПОЛУЧЕНИЕ СООБЩЕНИЙ ЧАТА
    // ======================================================

    public List<ChatMessageDto> getSessionMessages(Long sessionId, Principal principal) {

        User user = getUser(principal);
        ChatSession session = validateSessionOwner(sessionId, user);

        List<ChatMessage> messages =
                chatMessageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId);

        List<ChatMessageDto> result = new ArrayList<>();

        for (ChatMessage m : messages) {
            result.add(new ChatMessageDto(
                    m.getId(),
                    m.getRole(),
                    m.getContent(),
                    m.getCreatedAt()
            ));
        }

        return result;
    }

    // ======================================================
    // 4. ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    // ======================================================

    private User getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ChatSession validateSessionOwner(Long sessionId, User user) {
        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return session;
    }

    private ChatSession createNewSectionSession(User user, Long sectionId) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        ChatSession session = new ChatSession();
        session.setUser(user);
        session.setSection(section);
        session.setActive(true);
        session.setTitle("New chat");
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());

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

    private String buildPromptWithSection(Long sectionId, String question) {

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        return """
                You are an AI tutor.
                Use ONLY the lecture content below to answer.

                Lecture content:
                %s

                Student question:
                %s
                """.formatted(section.getContent(), question);
    }

    // ======================================================
    // 5. GEMINI API
    // ======================================================

    public  String generateAnswer(String prompt) {

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
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "AI error: " + e.getMessage();
        }
    }
}
