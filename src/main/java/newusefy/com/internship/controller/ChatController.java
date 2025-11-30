package newusefy.com.internship.controller;

import newusefy.com.internship.dto.ChatRequestDto;
import newusefy.com.internship.dto.ChatResponseDto;
import newusefy.com.internship.dto.ChatSessionSummaryDto;
import newusefy.com.internship.dto.ChatMessageDto;
import newusefy.com.internship.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * POST /api/chat
     * Главная точка входа для чата.
     * Принимает сообщение пользователя и (опционально) chatSessionId.
     * Возвращает ответ ИИ и sessionId.
     */
    @PostMapping
    public ChatResponseDto chat(@RequestBody ChatRequestDto request,
                                Principal principal) {
        return chatService.handleChat(request, principal);
    }

    /**
     * GET /api/chat/sessions
     * Возвращает список всех сессий (чатов) текущего пользователя.
     * Используется для списка "Chat history" в сайдбаре.
     */
    @GetMapping("/sessions")
    public List<ChatSessionSummaryDto> getUserSessions(Principal principal) {
        return chatService.getUserSessions(principal);
    }

    /**
     * GET /api/chat/sessions/{sessionId}
     * Возвращает все сообщения одной сессии чата.
     * Используется, когда ты кликаешь по элементу в "Chat history".
     */
    @GetMapping("/sessions/{sessionId}")
    public List<ChatMessageDto> getSessionMessages(@PathVariable Long sessionId,
                                                   Principal principal) {
        return chatService.getSessionMessages(sessionId, principal);
    }
}
