package com.vti.dto;

import lombok.Data;

import java.util.List;

@Data
public class LessonPageDTO {
    private List<LessonDTO> lessonDTOS;
    private Integer totalPage;
    private Long totalElement;
}
