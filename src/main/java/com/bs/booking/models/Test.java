package com.bs.booking.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@Entity
@Table(name = "test")
public class Test {

    @Id
    @Column(name = "test_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    private String name;
    private Integer age;
    private String description;
}
