package com.vti.service;

import com.vti.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICourseService {
     Page<Course> getListCourseByName(String courseName, Pageable pageable);
}
