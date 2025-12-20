package newusefy.com.internship.entity;

import jakarta.persistence.*;
import lombok.*;
import newusefy.com.internship.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // ⭐ МАППИНГ: многие пользователи могут быть записаны на много курсов
    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> users = new ArrayList<>();

}