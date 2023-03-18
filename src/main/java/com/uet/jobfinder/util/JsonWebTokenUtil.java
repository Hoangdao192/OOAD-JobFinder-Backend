package com.uet.jobfinder.util;

import javax.servlet.http.HttpServletRequest;

public class JsonWebTokenUtil {
    public String getJWTFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
//        System.out.println("TOKEN: " + token);
        if (token != null && !token.isEmpty() && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
