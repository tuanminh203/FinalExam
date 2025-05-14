package com.vti.controller;

import com.vti.dto.CourseDTO;
import com.vti.entity.Course;
import com.vti.reponsitory.CourseReponsitory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("courses")
public class CourseController {
    private final CourseReponsitory courseReponsitory;

    public CourseController(CourseReponsitory courseReponsitory) {
        this.courseReponsitory = courseReponsitory;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Course> coursePage = courseReponsitory.findAll(pageable);
        Page<CourseDTO> courseDTOPage = coursePage.map(course -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseId(course.getCourseId());
            courseDTO.setCourseName(course.getCourseName());
            courseDTO.setCourseDays(course.getCourseDays());
            return courseDTO;
        });
        return ResponseEntity.ok(courseDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Course> course = courseReponsitory.findById(id);
        if (course.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseId(course.get().getCourseId());
            courseDTO.setCourseName(course.get().getCourseName());
            courseDTO.setCourseDays(course.get().getCourseDays());
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
    public ResponseEntity<?> searchByName(@PathVariable String courseName, Pageable pageable) {
        Page<Course> coursePage = courseReponsitory.findByCourseName(courseName, pageable);
        Page<CourseDTO> courseDTOPage = coursePage.map(course -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseId(course.getCourseId());
            courseDTO.setCourseName(course.getCourseName());
            courseDTO.setCourseDays(course.getCourseDays());
            return courseDTO;
        });
       return ResponseEntity.ok(courseDTOPage);
    }
}

