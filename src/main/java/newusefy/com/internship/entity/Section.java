package newusefy.com.internship.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "course_id") // foreign key
    private Course course;

    @OneToOne
    @JoinColumn(name = "chat_session_id")
    private ChatSession chatSession;

}
