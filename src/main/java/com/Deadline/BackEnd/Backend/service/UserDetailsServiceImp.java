package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }


    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {

        return repository.findByUid(Long.parseLong(uid)).getFirst();
    }
}
