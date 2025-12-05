package newusefy.com.internship.dto;

import java.time.LocalDateTime;

/**
 * Короткое описание чата (сессии) для списка:
 * - id          -> понадобится, чтобы кликнуть и открыть чат
 * - title       -> заголовок (первые слова первого сообщения)
 * - updatedAt   -> чтобы отсортировать и показать "последние чаты"
 */
public class ChatSessionSummaryDto {

    private Long id;
    private String title;
    private LocalDateTime updatedAt;

    public ChatSessionSummaryDto() {
    }

    public ChatSessionSummaryDto(Long id, String title, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
