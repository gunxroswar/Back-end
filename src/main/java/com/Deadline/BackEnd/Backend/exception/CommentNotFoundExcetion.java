package com.Deadline.BackEnd.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundExcetion extends RuntimeException{
    public CommentNotFoundExcetion(Long commentId) {
        super("Comment not found with "+commentId);
    }
}
