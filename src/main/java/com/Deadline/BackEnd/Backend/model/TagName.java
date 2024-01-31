package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="TAG")
public class TagName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long tagID;
    @Column(nullable = false)
    private String tagName;
}