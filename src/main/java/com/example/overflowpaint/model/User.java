package com.example.overflowpaint.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long first;

    private Long second;
    private Long third;
    private Long forth;
    private Long fifth;
    private Long sixth;
    private Long seventh;
    private Long eighth;
    private Long ninth;

    private Long tenth;

    // Getters and setters
}

