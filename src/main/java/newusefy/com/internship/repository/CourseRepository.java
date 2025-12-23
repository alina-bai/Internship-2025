package newusefy.com.internship.repository;

import newusefy.com.internship.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

