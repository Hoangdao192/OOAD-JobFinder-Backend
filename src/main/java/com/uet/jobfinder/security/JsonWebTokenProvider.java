package com.uet.jobfinder.security;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.error.ServerError;
import com.uet.jobfinder.exception.CustomIllegalArgumentException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JsonWebTokenProvider {

    private static final String JWT_SECRET = "UET_JOB_FINDER";
    private static final Long JWT_EXPIRATION = 604800000L;

    public String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT clams string is empty");
        }
        return false;
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        return Long.parseLong(getUserIdFromJWT(
                getJWTFromRequest(request)
        ));
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new CustomIllegalArgumentException(
                    ServerError.INVALID_AUTHORIZATION
            );
        }

        return token.substring(7);
    }

}
