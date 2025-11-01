package com.jeespring.AIPortfolio.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Experience {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String role;
    private String startDate;
    private String endDate;
    private String description;
}