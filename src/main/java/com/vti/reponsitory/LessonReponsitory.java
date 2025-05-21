package com.vti.reponsitory;

import com.vti.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import static org.hibernate.sql.ast.Clause.FROM;

@Repository
public interface LessonReponsitory extends JpaRepository<Lesson, Integer> {
}
