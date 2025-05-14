package com.vti.service;

import com.vti.entity.Course;
import com.vti.reponsitory.CourseReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CourseService implements ICourseService {
    @Autowired
    private CourseReponsitory courseReponsitory;
    @Override
    public Page<Course> getListCourseByName(String courseName, Pageable pageable) {
        return this.courseReponsitory.findByCourseName(courseName, pageable);
    }
}
