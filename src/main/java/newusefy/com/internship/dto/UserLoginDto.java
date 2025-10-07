package newusefy.com.internship.dto;

/**
 * DTO for login requests.
 * Contains the raw username and password sent by the client.
 */
public class UserLoginDto {
    private String username;
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
