package com.vti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table
@Data
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lessonId;

    @Column(nullable = false, length = 100)
    private String lessonName;

    @Min(value = 1)
    private Integer lessonHours;

    @Min(value = 1)
    private Integer lessonDays;

    @Column(columnDefinition = "TEXT")
    private String lessonDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;
}
