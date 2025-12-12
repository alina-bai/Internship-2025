package newusefy.com.internship.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatSession session;

    private String role; // "user" или "ai"

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}
