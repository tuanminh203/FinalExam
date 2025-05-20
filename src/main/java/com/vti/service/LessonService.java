package com.vti.service;

import com.vti.entity.Lesson;
import com.vti.reponsitory.LessonReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LessonService implements ILessonService{
    @Autowired
    private LessonReponsitory lessonReponsitory;
    @Override
    public Page<Lesson> getLessonById(Integer lessonId, Pageable pageable) {
        return lessonReponsitory.findByLessonId(lessonId, pageable);
    }
}
