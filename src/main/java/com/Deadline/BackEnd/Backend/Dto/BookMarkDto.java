package com.Deadline.BackEnd.Backend.Dto;

import javax.validation.constraints.NotEmpty;

public class BookMarkDto {

    @NotEmpty
    public Long uid;

    public Long postId;


}
