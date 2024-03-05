package com.Deadline.BackEnd.Backend.Objects;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class editReply {
    private String replyID;
    private String commentID;
    private String topic;
    private String detail;
    private String cookie;
}
