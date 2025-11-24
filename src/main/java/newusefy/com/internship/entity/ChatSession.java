package newusefy.com.internship.entity;

import jakarta.persistence.*;
import lombok.*;
import newusefy.com.internship.model.User;     // ⭐ ВАЖНО: правильный импорт!
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Теперь ChatSession связывается с твоим User из пакета model
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();
}
