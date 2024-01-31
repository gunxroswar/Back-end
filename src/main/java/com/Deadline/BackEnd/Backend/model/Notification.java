package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATIONID")
    private Long NID;
    @Column(name = "DETAIL")
    private String detail;

    @Column( name = "STATUS" )
    private Long statusID;

    @Column(name= "REFERENCE")
    private  String reference;
}
