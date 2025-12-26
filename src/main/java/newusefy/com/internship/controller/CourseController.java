package newusefy.com.internship.controller;

import lombok.RequiredArgsConstructor;
import newusefy.com.internship.entity.Course;
import newusefy.com.internship.entity.Section;
import newusefy.com.internship.service.CourseService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 📌 1. Получить все курсы
    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    // 📌 2. Получить секции курса
    @GetMapping("/{courseId}/sections")
    public List<Section> getCourseSections(@PathVariable Long courseId) {
        return courseService.getSectionsForCourse(courseId);
    }
}
