package com.example.fitfolio.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fitfolio.component.JwtUtil;
import com.example.fitfolio.dto.LoginDto;
import com.example.fitfolio.dto.UserDto;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.service.UserService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/fitfolio")
public class UserController {
	

    public UserService userService;
    public UserController(UserService userService) {
		this.userService = userService;
	}
    
	@GetMapping(path = "/users")
  
    public List<Utilisateur> GetUsers() {
        return userService.listUsers();
    }
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }


    @PostMapping(path = "/addUser")
    public ResponseEntity<UserDto> saveUser(@RequestBody Utilisateur appUser) {
        UserDto userDto = userService.addNewUser(appUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }


    @PostMapping(path = "/addRole")
    public Role saveRole(@RequestBody Role appRole) {
        return userService.addNewRole(appRole);
    }

    @PostMapping(path = "/addRoleToUser")
    public  void addRoleToUser(@RequestBody LoginDto userRoleForm) {
    	userService.addRoleToUser(userRoleForm.getUser(), userRoleForm.getRole());
		return ;	
    }

    @GetMapping(path = "/refreshToken")
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("refresh");
        String authToken = request.getHeader("Authorization");
    
        JwtUtil jwtUtil = new JwtUtil(); 
        String userName; 
        Map<String, String> idToken = new HashMap<String, String>();
        idToken.put("Autorization Baerer saisie", authToken);
        if (authToken == null) {
        	System.out.println("authtoken nulle");
            return;
        }
        String jwt = authToken.substring(7); 
        DecodedJWT decodedJwt = jwtUtil.verifySignature(jwt); 
        if (decodedJwt == null) {
            System.out.println("JWT non valide");
            idToken.put("valide","JWT NON VALIDE");
            try {
            	response.setContentType("application/json");
				new ObjectMapper().writeValue(response.getOutputStream(), idToken);
			} catch (StreamWriteException e) {
				
				e.printStackTrace();
			} catch (DatabindException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
		
				e.printStackTrace();
			}
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        if (decodedJwt != null) {
            userName = jwtUtil.getUsername(decodedJwt);
            System.out.println("decoded"+userName);
            User user = (User) userService.loadUserByUsername(userName); 
            if (user == null) {
                System.out.println("Utilisateur non trouvé");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
           
            String accessToken = jwtUtil.generateAccessToken(userName); 
    
            idToken.put("refresh-token", jwt); 
            idToken.put("access-token", accessToken);
                try {
                   
                    response.setContentType("application/json"); 
                    new ObjectMapper().writeValue(response.getOutputStream(), idToken); 
                } catch (IOException e) {
                   
                    e.printStackTrace(); 
                }
    
        }
    }


    @GetMapping(path = "/user/{id}/bmi")
public ResponseEntity<Map<String, Object>> getUserBMI(@PathVariable Long id) {
    try {
        Double bmi = userService.calculateBMI(id);
        Map<String, Object> response = new HashMap<>();
        response.put("userId", id);
        response.put("bmi", bmi);
        return ResponseEntity.ok(response);
    } catch (RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", ex.getMessage()));
    }
}


    }





