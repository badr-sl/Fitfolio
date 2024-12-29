package com.example.fitfolio.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fitfolio.dto.UserDto;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.RoleRepository;
import com.example.fitfolio.repo.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository appUserRepository;

    @Autowired
    private RoleRepository appRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto addNewUser(Utilisateur appUser) {
        Utilisateur existingUser = appUserRepository.findByUsername(appUser.getUsername());
        UserDto appUserDto = new UserDto();
        String rawPassword = appUser.getPassword();

        if (existingUser != null) {
            // Preserve existing fields if they are not provided in the request
            appUser.setId(existingUser.getId());
            appUser.setPassword(
                    rawPassword != null ? bCryptPasswordEncoder.encode(rawPassword) : existingUser.getPassword()
            );
            appUser.setRoles(
                    appUser.getRoles() != null && !appUser.getRoles().isEmpty() ? appUser.getRoles() : existingUser.getRoles()
            );
            appUser.setPrenom(appUser.getPrenom() != null ? appUser.getPrenom() : existingUser.getPrenom());
            appUser.setNom(appUser.getNom() != null ? appUser.getNom() : existingUser.getNom());
            appUser.setTelephone(appUser.getTelephone() != null ? appUser.getTelephone() : existingUser.getTelephone());
            appUser.setAge(appUser.getAge() != 0 ? appUser.getAge() : existingUser.getAge());
            appUser.setPoids(appUser.getPoids() != null ? appUser.getPoids() : existingUser.getPoids());
            appUser.setTaille(appUser.getTaille() != null ? appUser.getTaille() : existingUser.getTaille());
            appUser.setSexe(appUser.getSexe() != null ? appUser.getSexe() : existingUser.getSexe());
            appUser.setObjectif(appUser.getObjectif() != null ? appUser.getObjectif() : existingUser.getObjectif());

            appUserDto.setMessage("Update performed");
        } else {
            // Encode the password for new user creation
            appUser.setPassword(bCryptPasswordEncoder.encode(rawPassword));
            appUserDto.setMessage("User created");
        }

        // Save the user and set the DTO
        appUserDto.setAppUser(appUserRepository.save(appUser));
        return appUserDto;
    }


    public Role addNewRole(Role role) {
        Role existingRole = appRoleRepository.findByRoleName(role.getRoleName());
        return (existingRole != null) ? existingRole : appRoleRepository.save(role);
    }

    
    public void addRoleToUser(String username, String rolename)  { 
    	System.out.println("le nom d'utilisateur : " + username+ "rolename"+rolename);
        
            Utilisateur appUser = appUserRepository.findByUsername(username);
            if (appUser == null) {
                System.out.println("Utilisateur introuvable avec le nom d'utilisateur : " + username);
                return ;
            }
            Role appRole = appRoleRepository.findByRoleName(rolename);
            if (appRole == null) {
                System.out.println("Rôle introuvable avec le nom : " + rolename);
                return;
            }

            if (appUser.getRoles().contains(appRole)) {
                
                System.out.println("L'utilisateur a déjà le rôle : " + rolename+appUser.getRoles()+"::::");
                
                return   ;
               
            }

           
            appUser.getRoles().add(appRole);
            System.out.println(" L'utilisateur " + username + " a reçu le rôle " + rolename);
          
            return ;
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur appUser = appUserRepository.findByUsername(username);
        return new User(appUser.getUsername(), appUser.getPassword(), getAuthorities(appUser.getRoles())); // Retourne les détails de l'utilisateur
    }


    public Collection<GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles == null ? Collections.emptyList() :
               roles.stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                    .collect(Collectors.toList());
    }
    
    public List<Utilisateur> listUsers() {
        return appUserRepository.findAll();
    }


    public Double calculateBMI(Long userId) {
        Utilisateur user = appUserRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        if (user.getTaille() == null || user.getPoids() == null || user.getTaille() == 0) {
            throw new RuntimeException("User data for BMI calculation is incomplete.");
        }
    
        // BMI Formula: weight (kg) / (height (m) * height (m))
        double heightInMeters = user.getTaille() / 100; // Convert height from cm to meters
        return user.getPoids() / (heightInMeters * heightInMeters);
    }

    public ResponseEntity<UserDto> login(String email, String password) {
        Utilisateur appUser = appUserRepository.findByUsername(email);

        // If user not found
        if (appUser == null) {
            UserDto userDto = new UserDto();
            userDto.setMessage("User not found with email: " + email);
            return new ResponseEntity<>(userDto, HttpStatus.UNAUTHORIZED);
        }

        // Check password
        if (!bCryptPasswordEncoder.matches(password, appUser.getPassword())) {
            UserDto userDto = new UserDto();
            userDto.setMessage("Invalid password");
            return new ResponseEntity<>(userDto, HttpStatus.UNAUTHORIZED);
        }

        // Create a DTO for the response
        UserDto userDto = new UserDto();
        userDto.setMessage("Login successful");
        userDto.setAppUser(appUser);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
