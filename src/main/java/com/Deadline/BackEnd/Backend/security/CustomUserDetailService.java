package com.Deadline.BackEnd.Backend.security;

import com.Deadline.BackEnd.Backend.model.Role;
import com.Deadline.BackEnd.Backend.model.User;
import com.Deadline.BackEnd.Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;

    }

//    public UserDetails loadUserByUid(Long uid) throws UsernameNotFoundException {
//
//        User user = userRepository.findByUid(uid).orElseThrow(()
//                -> new UsernameNotFoundException("Username not found"));
//
//        return (UserDetails) new User(user.getUid(), user.getImageDir()/*, user.getRoles()*/, user.getProfileName(),user.getUsername(),
//                user.getPassword(),user.getUserStatus(),user.getPosts(),user.getBookmarkPosts());
//    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
