package newusefy.com.internship.controller;

import jakarta.validation.Valid;
import newusefy.com.internship.dto.ChatRequestDto;
import newusefy.com.internship.dto.ChatResponseDto;
import newusefy.com.internship.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat(@Valid @RequestBody ChatRequestDto body) {
        String aiReply = chatService.chat(body.getMessage());
        return ResponseEntity.ok(new ChatResponseDto(aiReply));
    }
}
