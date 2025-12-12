package newusefy.com.internship.dto;

public class ChatRequestDto {

    // сообщение пользователя
    private String prompt;

    // если null → создаём новую сессию
    // если есть ID → продолжаем существующую
    private Long chatSessionId;

    public ChatRequestDto() {}

    public ChatRequestDto(String prompt, Long chatSessionId) {
        this.prompt = prompt;
        this.chatSessionId = chatSessionId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Long getChatSessionId() {
        return chatSessionId;
    }

    public void setChatSessionId(Long chatSessionId) {
        this.chatSessionId = chatSessionId;
    }
}
