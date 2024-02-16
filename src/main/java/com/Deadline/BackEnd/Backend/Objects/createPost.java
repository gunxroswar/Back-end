package com.Deadline.BackEnd.Backend.Objects;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class createPost {
    private String topic;
    private String tag;
    private String detail;
    private String cookie;
}
