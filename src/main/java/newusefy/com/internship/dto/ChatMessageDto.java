package newusefy.com.internship.dto;

import java.time.LocalDateTime;

/**
 * Сообщение внутри конкретной сессии:
 * - id        -> может пригодиться в будущем (удаление, редактирование)
 * - role      -> "user" или "ai"
 * - content   -> текст сообщения
 * - createdAt -> время отправки
 */
public class ChatMessageDto {

    private Long id;
    private String role;
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageDto() {
    }

    public ChatMessageDto(Long id, String role, String content, LocalDateTime createdAt) {
        this.id = id;
        this.role = role;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
