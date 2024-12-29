package com.example.fitfolio.component;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String SECRET_KEY = "J0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGkiLCJleHAiOjE3MzIwNDQxOTcsImlhdCI6MTczMjA0Mzg5N30.8Y";

    public String generateAccessToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 300))
                .sign(algorithm);
    }

    public String createRefreshToken(String subject) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 300))
                .sign(algorithm);
    }

    // Vérification du token
    public DecodedJWT verifySignature(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        } catch (Exception e) {
        	System.out.println("attention token invalide ou expire"+token);
            return null;
        }
    }
    
    public String[] getRoles(DecodedJWT decodedJwt) {
        return decodedJwt.getClaim("roles").asArray(String.class);
    }


    public String getUsername(DecodedJWT decodedJwt) {
        return decodedJwt.getSubject();
    }
    
}

