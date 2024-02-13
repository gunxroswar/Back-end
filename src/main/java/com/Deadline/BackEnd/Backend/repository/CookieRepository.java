package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CookieRepository extends JpaRepository<Cookie,Long> {
    Optional<Cookie> findByCookieId(Long id);
    Optional<Cookie> findByUser(String user);
    Optional<Cookie> findByCookie(Long cookie);
    Optional<Cookie> findCookieWithMaxId();
}
