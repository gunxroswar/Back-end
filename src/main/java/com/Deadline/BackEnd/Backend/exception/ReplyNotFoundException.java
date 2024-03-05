package com.Deadline.BackEnd.Backend.exception;

public class ReplyNotFoundException extends RuntimeException {

    public ReplyNotFoundException(Long replyId)
    {
        super("Reply found with "+replyId);
    }

}
