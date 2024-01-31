package com.Deadline.BackEnd.Backend.exception;


import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.Authenticator;

@ResponseStatus
public class InvalidJwtException extends AuthenticationException {

    public InvalidJwtException(String ex){
        super(ex);
    }
}
