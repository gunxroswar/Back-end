package com.Deadline.BackEnd.Backend.exception;


import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookmarkNotFoundException extends RuntimeException {

    public BookmarkNotFoundException(User user){
        super("Error: User " + user.getUsername() + " does not have any bookmarked posts");
    }
}

