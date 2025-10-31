package newusefy.com.internship.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatRequestDto {
    @NotBlank(message = "message cannot be blank")
    private String message;

    public ChatRequestDto() {}
    public ChatRequestDto(String message) { this.message = message; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
