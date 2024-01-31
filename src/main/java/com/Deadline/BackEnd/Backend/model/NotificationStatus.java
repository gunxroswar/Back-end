package com.Deadline.BackEnd.Backend.model;

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

    private Long notificationstatusID;
    @Column(nullable = false)
    private String notificationstatusName;
}
