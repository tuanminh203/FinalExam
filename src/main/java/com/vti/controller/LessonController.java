package com.vti.controller;

import com.vti.dto.LessonDTO;
import com.vti.dto.LessonPageDTO;
import com.vti.entity.Lesson;
import com.vti.reponsitory.LessonReponsitory;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
    private final LessonReponsitory lessonReponsitory;
    private final ModelMapper modelMapper;

    public LessonController(LessonReponsitory lessonReponsitory, ModelMapper modelMapper) {
        this.lessonReponsitory = lessonReponsitory;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Lesson> lessonPage = lessonReponsitory.findAll(pageable);
        List<Lesson> lessons = lessonPage.getContent();
        List<LessonDTO> lessonDTOS = modelMapper.map(lessons,
                new TypeToken<List<LessonDTO>>() {}.getType());
        LessonPageDTO lessonDTOPage = new LessonPageDTO();
        lessonDTOPage.setLessonDTOS(lessonDTOS);
        lessonDTOPage.setTotalPage(lessonPage.getTotalPages());
        lessonDTOPage.setTotalElement(lessonPage.getTotalElements());

        return ResponseEntity.ok(lessonDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<Lesson> optionalLesson = lessonReponsitory.findById(id);
        if(optionalLesson.isEmpty()) {
            return ResponseEntity.badRequest().body("Lesson not found: " +id);
        }

        LessonDTO lessonDTO = modelMapper.map(optionalLesson.get(), LessonDTO.class);
        return ResponseEntity.ok(lessonDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody LessonDTO lessonDTO) {
        if(lessonDTO.getLessonName() == null){
            return ResponseEntity.badRequest().body("Lesson name is required");
        }

        Lesson existingLesson = lessonReponsitory.findById(id).orElse(null);
        if (existingLesson == null) {
            return ResponseEntity.badRequest().body("Lesson not found: " +id);
        }

        existingLesson.setLessonName(lessonDTO.getLessonName());
        existingLesson.setLessonHours(lessonDTO.getLessonHours());
        existingLesson.setLessonDays(lessonDTO.getLessonDays());
        existingLesson.setLessonDescription(lessonDTO.getLessonDescription());

        Lesson updatedLesson = lessonReponsitory.save(existingLesson);
        return ResponseEntity.ok(updatedLesson);
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
