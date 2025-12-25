package newusefy.com.internship.service;

import lombok.RequiredArgsConstructor;
import newusefy.com.internship.entity.Course;
import newusefy.com.internship.entity.Section;
import newusefy.com.internship.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Section> getSectionsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return course.getSections(); // 😊 Легко!
    }
}
