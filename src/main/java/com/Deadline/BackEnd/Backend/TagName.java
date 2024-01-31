package com.Deadline.BackEnd.Backend;

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
    @Column(name = "TAGID")
    private Long tagID;
    @Column(name = "TAGNAME")
    private String tagName;
}
