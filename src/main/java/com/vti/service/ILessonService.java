package com.vti.service;

import com.vti.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ILessonService {
    Page<Lesson> getLessonById(Integer lessonId, Pageable pageable);
}
