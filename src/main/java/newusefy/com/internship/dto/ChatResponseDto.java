package newusefy.com.internship.dto;

/**
 * Ответ бэкенда на запрос /api/chat
 * - response      -> текст ответа ИИ
 * - chatSessionId -> ID сессии (чтобы фронт мог продолжать тот же чат)
 */
public class ChatResponseDto {

    private String response;
    private Long chatSessionId;

    public ChatResponseDto() {
    }

    public ChatResponseDto(String response, Long chatSessionId) {
        this.response = response;
        this.chatSessionId = chatSessionId;
    }

    // --- геттеры/сеттеры ---

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(Long chatSessionId) {
        this.chatSessionId = chatSessionId;
    }
}
