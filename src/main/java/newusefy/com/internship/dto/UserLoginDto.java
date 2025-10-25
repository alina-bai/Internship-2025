package newusefy.com.internship.dto;

import jakarta.validation.constraints.NotBlank;

public class UserLoginDto {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public UserLoginDto() {}

    public UserLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
