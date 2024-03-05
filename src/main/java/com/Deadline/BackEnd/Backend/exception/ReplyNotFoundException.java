package com.Deadline.BackEnd.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ReplyNotFoundException extends RuntimeException{
    public ReplyNotFoundException(Long replyId){super("Reply found with "+ replyId);}
}
