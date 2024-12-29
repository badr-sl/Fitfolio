package ServiceTest;

import com.example.fitfolio.dto.UserDto;
import com.example.fitfolio.entities.Role;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.RoleRepository;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewUser_CreateUser() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.addNewUser(user);

        assertEquals("User created", result.getMessage());
        assertEquals(user, result.getAppUser());
    }

    @Test
    void testAddNewUser_UpdateUser() {
        Utilisateur existingUser = new Utilisateur();
        existingUser.setId(1L);
        existingUser.setUsername("testuser");
        existingUser.setPassword("oldPassword");

        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword("newPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(existingUser);
        when(bCryptPasswordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.addNewUser(user);

        assertEquals("Update performed", result.getMessage());
        assertEquals(user, result.getAppUser());
    }

    @Test
    void testAddNewRole_CreateRole() {
        Role role = new Role(null, "ROLE_USER");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(null);
        when(roleRepository.save(role)).thenReturn(role);

        Role result = userService.addNewRole(role);

        assertEquals(role, result);
    }

    @Test
    void testAddNewRole_RoleExists() {
        Role existingRole = new Role(1L, "ROLE_USER");

        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(existingRole);

        Role result = userService.addNewRole(existingRole);

        assertEquals(existingRole, result);
    }

    @Test
    void testAddRoleToUser_Success() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setRoles(new ArrayList<>());

        Role role = new Role(1L, "ROLE_USER");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(role);

        userService.addRoleToUser("testuser", "ROLE_USER");

        verify(userRepository, times(1)).findByUsername("testuser");
        assertTrue(user.getRoles().contains(role));
    }

    @Test
    void testLoadUserByUsername_Success() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Collections.singletonList(new Role(1L, "ROLE_USER")));

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    void testListUsers() {
        List<Utilisateur> users = new ArrayList<>();
        users.add(new Utilisateur());

        when(userRepository.findAll()).thenReturn(users);

        List<Utilisateur> result = userService.listUsers();

        assertEquals(1, result.size());
    }

    @Test
    void testCalculateBMI_Success() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPoids(70.0);
        user.setTaille(170.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Double bmi = userService.calculateBMI(1L);

        assertNotNull(bmi);
        assertEquals(24.22, bmi, 0.01);
    }

    @Test
    void testCalculateBMI_IncompleteData() {
        Utilisateur user = new Utilisateur();
        user.setId(1L);
        user.setPoids(70.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(RuntimeException.class, () -> userService.calculateBMI(1L));

        assertEquals("User data for BMI calculation is incomplete.", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bCryptPasswordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        ResponseEntity<UserDto> response = userService.login("testuser", "password");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().getMessage());
    }

    @Test
    void testLogin_InvalidPassword() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(bCryptPasswordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseEntity<UserDto> response = userService.login("testuser", "wrongPassword");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid password", response.getBody().getMessage());
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        ResponseEntity<UserDto> response = userService.login("unknown", "password");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found with email: unknown", response.getBody().getMessage());
    }
    @Test
    void testAddRoleToUser_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        // Act
        userService.addRoleToUser("unknownUser", "ROLE_USER");

        // Assert
        verify(userRepository, times(1)).findByUsername("unknownUser");
        verify(roleRepository, never()).findByRoleName(anyString());
        // Ensure no exceptions or changes occur for non-existing user
    }

    @Test
    void testAddRoleToUser_RoleNotFound() {
        // Arrange
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setRoles(new ArrayList<>());

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(roleRepository.findByRoleName("ROLE_UNKNOWN")).thenReturn(null);

        // Act
        userService.addRoleToUser("testuser", "ROLE_UNKNOWN");

        // Assert
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(roleRepository, times(1)).findByRoleName("ROLE_UNKNOWN");
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testAddRoleToUser_RoleAlreadyAssigned() {
        // Arrange
        Role role = new Role(1L, "ROLE_USER");
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setRoles(new ArrayList<>());
        user.getRoles().add(role);

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(role);

        // Act
        userService.addRoleToUser("testuser", "ROLE_USER");

        // Assert
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(roleRepository, times(1)).findByRoleName("ROLE_USER");
        assertEquals(1, user.getRoles().size()); // Ensure role is not added again
    }

}
