package newusefy.com.internship.dto;

// Если вы используете Lombok, вам просто нужно добавить @Data,
// но я предоставляю явный код для максимальной совместимости.

public class ChatRequestDto {
    // ✅ Фронтенд отправляет это поле!
    private String prompt;

    // Конструктор по умолчанию (необходим для Spring/Jackson)
    public ChatRequestDto() {
    }

    // Конструктор со всеми полями
    public ChatRequestDto(String prompt) {
        this.prompt = prompt;
    }

    // ✅ Геттер, необходимый для ChatController
    public String getPrompt() {
        return prompt;
    }

    // Сеттер (для Jackson)
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
