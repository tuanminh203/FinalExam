package com.vti.controller;

import com.vti.dto.LessonDTO;
import com.vti.entity.Lesson;
import com.vti.reponsitory.LessonReponsitory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    private final LessonReponsitory lessonReponsitory;
    public LessonController(LessonReponsitory lessonReponsitory) {
        this.lessonReponsitory = lessonReponsitory;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id,
                                      Pageable pageable) {
        Page<Lesson> lessonPage = lessonReponsitory.findByLessonId(id, pageable);
        Page<LessonDTO> lessonDTOPage = lessonPage.map(lesson -> {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setLessonId(lesson.getLessonId());
            lessonDTO.setLessonName(lesson.getLessonName());
            lessonDTO.setLessonHours(lesson.getLessonHours());
            lessonDTO.setLessonDescription(lesson.getLessonDescription());
            return lessonDTO;
        });
        return ResponseEntity.ok(lessonDTOPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody LessonDTO lessonDTO) {
        Lesson lesson = lessonReponsitory.findById(id).orElse(null);
        if (lesson == null) {
            return ResponseEntity.badRequest().body("Lesson not found: " +id);
        }
        lesson.setLessonName(lessonDTO.getLessonName());
        lesson.setLessonHours(lessonDTO.getLessonHours());
        lesson.setLessonDescription(lessonDTO.getLessonDescription());
        lessonReponsitory.save(lesson);
        return ResponseEntity.ok("Lesson updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Lesson lesson = lessonReponsitory.findById(id).orElse(null);
        if (lesson == null) {
            return ResponseEntity.badRequest().body("Lesson not found: " +id);
        }
        lessonReponsitory.delete(lesson);
        return ResponseEntity.ok("Lesson deleted successfully");
    }
}
