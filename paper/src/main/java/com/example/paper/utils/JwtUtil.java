package com.example.paper.utils;

import com.example.paper.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "ningeraweet";

    public static String generateToken(User user) {
        HashMap<String, Object> map = new HashMap<>();
        //you can put any data in the map
        map.put("account", user.getAccount());
        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + 3600_000L))// 1 hour
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
        return "Bearer "+jwt; //jwt前面一般都会加Bearer
    }

    public static Map<String,Object> validateToken(String token) {
        try {
            // parse the token.
            return Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace("Bearer ",""))
                    .getBody();
        }catch (Exception e){
            throw new IllegalStateException("Invalid Token. "+e.getMessage());
        }
    }
}