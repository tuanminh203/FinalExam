package com.vti.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer courseId;

    @Column(nullable = false, length = 100)
    private String courseName;

    @Column(nullable = false)
    private Integer courseHours;

    @Column(nullable = false)
    private Integer courseDays;

    @Column(columnDefinition = "TEXT")
    private String courseDescription;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

}
