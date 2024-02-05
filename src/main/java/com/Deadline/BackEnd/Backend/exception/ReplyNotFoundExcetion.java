package com.Deadline.BackEnd.Backend.exception;

public class ReplyNotFoundExcetion extends RuntimeException {

    public ReplyNotFoundExcetion(Long replyId)
    {
        super("Reply found with "+replyId);
    }

}
