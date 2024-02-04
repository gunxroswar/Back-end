package com.Deadline.BackEnd.Backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Entity
@Data
@Getter
@Setter
@Table(name = "Cookie")
public class Cookie {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private int cookieId;

 @OneToOne
 @JoinColumn(name = "userId" , referencedColumnName = "uid")
 private User user;

 @Column(unique = true,nullable = false)
 private String cookie;
}
