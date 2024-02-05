package com.Deadline.BackEnd.Backend.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="SQLInjectionTest")
public class SQLInjectionTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int testID;

    @Column(nullable = false)
    private String testValue;
}
