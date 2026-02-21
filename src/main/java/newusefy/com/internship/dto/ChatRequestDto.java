package newusefy.com.internship.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
