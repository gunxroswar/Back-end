package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name="NOTIFICATION")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long notificationID;
    @Column(nullable = false)
    private String detail;


    @Column(nullable = false)
    private  String reference;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="statusId")
    private NotificationStatus notificationStatus;
}
