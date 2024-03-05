package com.Deadline.BackEnd.Backend.Dto;

import javax.validation.constraints.NotEmpty;

public class GetBookMarkDto {

    @NotEmpty
    public Long uid;
    @NotEmpty
    public Long postId;

}
