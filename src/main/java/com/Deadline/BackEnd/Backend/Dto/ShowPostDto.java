package com.Deadline.BackEnd.Backend.Dto;

import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowPostDto {
    private Long postId;
    private Long UID;
    private String name;
    private String topic;
    private String detail;
    private Boolean anonymous;
    private List<Map<String,String>> taglist;
    private Boolean hasVerify;
    private String status;
    private Long likeCount;
    private Date createAt;


}

