package com.vti.controller;

import com.vti.dto.CourseDTO;
import com.vti.dto.CoursePageDTO;
import com.vti.entity.Course;
import com.vti.entity.Lesson;
import com.vti.reponsitory.CourseReponsitory;
import com.vti.reponsitory.LessonReponsitory;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final ModelMapper modelMapper;
    private final CourseReponsitory courseReponsitory;
    private final LessonReponsitory lessonReponsitory;

    public CourseController(ModelMapper modelMapper, CourseReponsitory courseReponsitory, LessonReponsitory lessonReponsitory) {
        this.modelMapper = modelMapper;
        this.courseReponsitory = courseReponsitory;
        this.lessonReponsitory = lessonReponsitory;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Course> coursePage = courseReponsitory.findAll(pageable);
        List<Course> courses = coursePage.getContent();
        List<CourseDTO> courseDTOS = modelMapper.map(courses,
                                                     new TypeToken<List<CourseDTO>>() {}.getType());
        CoursePageDTO coursePageDTO = new CoursePageDTO();
        coursePageDTO.setCourseDTOS(courseDTOS);
        coursePageDTO.setTotalPage(coursePage.getTotalPages());
        coursePageDTO.setTotalElement(coursePage.getTotalElements());
        return ResponseEntity.ok(coursePageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Course> course = courseReponsitory.findById(id);
        if (course.isEmpty()) {
            return ResponseEntity.badRequest().body("Course not found: " +id);
        } else {
            CourseDTO courseDTO = modelMapper.map(course.get(), CourseDTO.class);
            return ResponseEntity.ok(courseDTO);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course course) {
        if (course.getCourseName() == null || course.getCourseDays() <= 0) {
            return ResponseEntity.badRequest().body("Invalid course data");
        }
        Course savedCourse = courseReponsitory.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Course course) {
        if (course.getCourseName() == null || course.getCourseDays() <= 0) {
            return ResponseEntity.badRequest().body("Invalid course data");
        }
        Optional<Course> courseOptional = courseReponsitory.findById(id);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Course existingCourse = courseOptional.get();
            existingCourse.setCourseName(course.getCourseName());
            existingCourse.setCourseDays(course.getCourseDays());
            courseReponsitory.save(existingCourse);
            return ResponseEntity.ok(existingCourse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<Course> courseOptional = courseReponsitory.findById(id);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            courseReponsitory.deleteById(id);
            return ResponseEntity.ok("Deleted successfully");
        }
    }

    @GetMapping("/search/{courseName}")
    public ResponseEntity<?> searchByName(@PathVariable String courseName,
                                          Pageable pageable) {
        Page<Course> coursePage = courseReponsitory.findByCourseName(courseName, pageable);
        List<Course> courses = coursePage.getContent();
        List<CourseDTO> courseDTOS = modelMapper.map(courses,
                new TypeToken<List<CourseDTO>>() {}.getType());
        CoursePageDTO coursePageDTO = new CoursePageDTO();
        coursePageDTO.setCourseDTOS(courseDTOS);
        coursePageDTO.setTotalPage(coursePage.getTotalPages());
        coursePageDTO.setTotalElement(coursePage.getTotalElements());
        return ResponseEntity.ok(coursePageDTO);
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<?> addLesson(@PathVariable Integer courseId,
                                       @RequestBody Lesson lesson) {
        Course course = courseReponsitory.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.badRequest().body("Course not found: " + courseId);
        }

        lesson.setCourse(course);
        lessonReponsitory.save(lesson);
        Lesson savedLesson = lessonReponsitory.save(lesson);
        return ResponseEntity.ok(savedLesson);
    }
}

