package com.vti.dto;

import lombok.Data;

import java.util.List;

@Data
public class CoursePageDTO {
    private List<CourseDTO> courseDTOS;
    private Integer totalPage;
    private Long totalElement;
}
