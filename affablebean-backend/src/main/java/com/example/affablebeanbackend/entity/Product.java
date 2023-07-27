package com.example.affablebeanbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double price;
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private LocalDateTime lastUpdate;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
