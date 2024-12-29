package com.example.fitfolio.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.UserService;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager; 
    private final UserService accountService;
    private final JwtUtil jwtUtil; 
    private final UserRepository userRepo;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService accountService,
                                   JwtUtil jwtUtil, UserRepository userRepo) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
        this.userRepo=userRepo;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDetails user = null;
        try {

            user = accountService.loadUserByUsername(username);
        } catch (Exception e) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return null;
        }

        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                response.getWriter().write("User not found: " + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password, user.getAuthorities());


        setDetails(request, authenticationToken);


        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetails user = (UserDetails) authResult.getPrincipal();
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());
        Utilisateur  appuser=userRepo.findByUsername(user.getUsername());
        Map<String, Object> idToken = new HashMap<>();
        idToken.put("refresh-token", refreshToken);
        idToken.put("access-token", accessToken);
        idToken.put("user", appuser);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Login ou mot de passe invalide: " + failed.getMessage());
    }
}
