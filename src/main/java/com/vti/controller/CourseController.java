package com.vti.controller;

import com.vti.dto.CourseDTO;
import com.vti.dto.CoursePageDTO;
import com.vti.dto.LessonDTO;
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
        Optional<Course> courseOptional = courseReponsitory.findById(id);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Course not found: " +id);
        }
            CourseDTO courseDTO = modelMapper.map(courseOptional.get(), CourseDTO.class);
            return ResponseEntity.ok(courseDTO);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseDTO courseDTO) {
        if(courseDTO.getCourseName() == null) {
            return ResponseEntity.badRequest().body("Course name is required");
        } else if (courseDTO.getCourseDays() <1) {
            return ResponseEntity.badRequest().body("Course days must be greater than 0");
        } else if (courseDTO.getCourseHours() <1) {
            return ResponseEntity.badRequest().body("Course hours must be greater than 0");
        } else if (courseReponsitory.existsByCourseName(courseDTO.getCourseName())) {
            return ResponseEntity.badRequest().body("Course name already exists");
        }

        CourseDTO courseDTOS = modelMapper.map(courseDTO, CourseDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody CourseDTO courseDTO) {
        if (courseDTO.getCourseDays() <1) {
            return ResponseEntity.badRequest().body("Course days must be greater than 0");
        } else if (courseDTO.getCourseHours() <1) {
            return ResponseEntity.badRequest().body("Course hours must be greater than 0");
        }

        Course existingCourse = courseReponsitory.findById(id).orElse(null);
        if (existingCourse == null) {
            return ResponseEntity.badRequest().body("Course not found with id: " + id);
        }

        // Chỉ cập nhật các trường cần thiết
        existingCourse.setCourseName(courseDTO.getCourseName());
        existingCourse.setCourseDays(courseDTO.getCourseDays());
        existingCourse.setCourseHours(courseDTO.getCourseHours());
        existingCourse.setCourseDescription(courseDTO.getCourseDescription());

        Course updated = courseReponsitory.save(existingCourse);
        return ResponseEntity.ok(updated);
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
                                       @RequestBody LessonDTO lessonDTO) {
        Course course = courseReponsitory.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.badRequest().body("Course not found: " + courseId);
        }

        Lesson lessonDTOS = modelMapper.map(lessonDTO, Lesson.class);
        lessonDTOS.setCourse(course);

        //Save lesson
        Lesson savedLesson = lessonReponsitory.save(lessonDTOS);
        return ResponseEntity.ok(savedLesson);
    }
}

