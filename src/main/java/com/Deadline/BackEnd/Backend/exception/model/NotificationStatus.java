package com.Deadline.BackEnd.Backend.exception.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="NOTIFICATIONSTATUS")
public class NotificationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATIONSTATUSID")
    private Long notificationstatusID;
    @Column(name = "NOTIFICATIONSTATUSNAME")
    private String notificationstatusName;
}
