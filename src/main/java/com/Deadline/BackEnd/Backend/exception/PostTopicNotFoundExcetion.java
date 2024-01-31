package com.Deadline.BackEnd.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PostTopicNotFoundExcetion extends RuntimeException{
    public  PostTopicNotFoundExcetion(Long postID){super("not found with "+postID);}
}
