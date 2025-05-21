package com.vti.reponsitory;

import com.vti.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonReponsitory extends JpaRepository<Lesson, Integer> {
}
