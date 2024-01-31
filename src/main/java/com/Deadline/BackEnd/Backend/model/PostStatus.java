package com.Deadline.BackEnd.Backend.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="POSTSTATUS")
public class PostStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTSTATUSID")
    private Long notificationStatusID;
    @Column(name = "POSTSTATUSNAME")
    private String postStatusName;
}
