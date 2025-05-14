package com.vti.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Integer lessonId;
    private String lessonName;
    private String lessonDescription;
    private Integer lessonHours;;
}
