package com.Deadline.BackEnd.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostNotFoundExcetion extends RuntimeException{
    public PostNotFoundExcetion(long postId){super("Post found with "+ postId);}
}
