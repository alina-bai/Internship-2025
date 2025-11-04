package newusefy.com.internship.controller;

import newusefy.com.internship.dto.ChatRequestDto;
import newusefy.com.internship.dto.ChatResponseDto;
import newusefy.com.internship.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ChatResponseDto chat(@RequestBody ChatRequestDto request) {
        // ✅ ИСПРАВЛЕНИЕ: Используем getPrompt() вместо getMessage()
        // Предполагается, что ChatRequestDto имеет поле 'prompt'
        String reply = chatService.chat(request.getPrompt());
        return new ChatResponseDto(reply);
    }
}
