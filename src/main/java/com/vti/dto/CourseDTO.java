package com.vti.dto;

import com.vti.entity.Lesson;
import lombok.Data;

@Data
public class CourseDTO {
    private String courseName;
    private Integer courseHours;
    private Integer courseDays;
    private String courseDescription;
}
