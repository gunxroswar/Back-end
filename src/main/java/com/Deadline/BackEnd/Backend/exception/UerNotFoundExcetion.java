package com.Deadline.BackEnd.Backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UerNotFoundExcetion extends  RuntimeException{
    public UerNotFoundExcetion(Integer id){
        super("User not found with"+id);
    }

}
