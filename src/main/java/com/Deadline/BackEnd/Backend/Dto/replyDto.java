package com.Deadline.BackEnd.Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class replyDto {
    private Long replyId;
    @NotEmpty
    private Long commentId;
    @NotEmpty
    private String topic;
    @NotEmpty
    private String detail;
    @NotEmpty
    private Long UID;
    private Boolean anonymous;
    private String status;
    private Date createAt;
}
