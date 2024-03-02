package com.Deadline.BackEnd.Backend.service;

import com.Deadline.BackEnd.Backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "DDB96A29437FDD4D3ADF508B5AF7FCA23EDC40FE22BE41B80A601693B5DB6AF9CE6DA092E98DC53CAC4194F668F249D72089ECF9C440DECB085AE1F52DB0DC5B";


    public String generateToken(User user){
        return Jwts
                .builder()
                .setSubject(user.getUid().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()  ))
                .setExpiration(new Date(System.currentTimeMillis() + 24 *60*60*1000 ))
                .signWith(getSigninKey())
                .compact();
    }

    public String extractUID(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token , UserDetails user){
        String UID = extractUID(token);
        return (UID.equals(user.getUsername())) && !isTokenExpires(token);

    }

    private boolean isTokenExpires(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T > resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
