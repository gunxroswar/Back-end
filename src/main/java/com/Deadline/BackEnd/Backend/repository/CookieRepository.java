package com.Deadline.BackEnd.Backend.repository;

import com.Deadline.BackEnd.Backend.model.Cookie;
import com.Deadline.BackEnd.Backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CookieRepository extends JpaRepository<Cookie,Long> {
    Optional<Cookie> findByCookieId(Long id);
    Optional<Cookie> findByUser(User user);
    Optional<Cookie> findByCookie(String cookie);

    //@Query("SELECT MAX(cookieId) FROM cookie")
    //Optional<Cookie> findCookieWithMaxId();

    @Query("SELECT coalesce(max(cookieId), 0) FROM Cookie")
    int findMaxId();
}
