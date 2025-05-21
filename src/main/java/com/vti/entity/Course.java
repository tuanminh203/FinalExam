package com.vti.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false, unique = true, length = 100)
    @NotBlank
    private String courseName;

    @Min(value = 1)
    private Integer courseHours;

    @Min(value = 1)
    private Integer courseDays;

    @Column(columnDefinition = "TEXT")
    private String courseDescription;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

}
