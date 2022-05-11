package com.example.articleProgram.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.servlet.http.HttpServletRequest;

import static java.util.Arrays.stream;

@EnableWebSecurity
public class JWTDecoder {

    private final HttpServletRequest httpServletRequest;

    public JWTDecoder(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public String getUsernameofLoggedInPerson () {
        String [] accessToken = httpServletRequest.getHeader("Authorization").split(" ");
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = JWT.decode(accessToken[1]);
        String username = decodedJWT.getSubject();
        return username;
    }

//    public Collection<SimpleGrantedAuthority> getRoles (String accessToken) {
//        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
//        DecodedJWT decodedJWT = JWT.decode(accessToken);
//        String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
//        return authorities;
//    }
}