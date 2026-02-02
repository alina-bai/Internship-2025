package newusefy.com.internship.dto;

public class ChatRequestDto {

    // сообщение пользователя
    private String prompt;

    // если null → создаём новую сессию
    // если есть ID → продолжаем существующую
    private Long chatSessionId;

    // 🔥 NEW — секция (лекция), внутри которой идёт чат
    private Long sectionId;

    public ChatRequestDto() {}

    public ChatRequestDto(String prompt, Long chatSessionId, Long sectionId) {
        this.prompt = prompt;
        this.chatSessionId = chatSessionId;
        this.sectionId = sectionId;
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

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }
}
