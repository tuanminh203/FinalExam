package com.vti.reponsitory;

import com.vti.entity.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseReponsitory extends JpaRepository<Course, Integer> {
    Page<Course> findByCourseName(String courseName, Pageable pageable);
    boolean existsByCourseName(@NotBlank String courseName);
}
