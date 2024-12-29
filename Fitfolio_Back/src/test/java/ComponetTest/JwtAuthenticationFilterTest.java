package ComponetTest;

import com.example.fitfolio.component.JwtAuthenticationFilter;
import com.example.fitfolio.component.JwtUtil;
import com.example.fitfolio.entities.Utilisateur;
import com.example.fitfolio.repo.UserRepository;
import com.example.fitfolio.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    private TestJwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new TestJwtAuthenticationFilter(authenticationManager, userService, jwtUtil, userRepository);
    }

    @Test
    void testAttemptAuthenticationSuccess() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(request.getParameter("username")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password");

        User mockUser = new User("testUser", "password", Collections.emptyList());
        when(userService.loadUserByUsername("testUser")).thenReturn(mockUser);

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        // Act
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // Assert
        assertNotNull(result);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAttemptAuthenticationUserNotFound() throws IOException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(request.getParameter("username")).thenReturn("unknownUser");
        when(request.getParameter("password")).thenReturn("password");

        when(userService.loadUserByUsername("unknownUser")).thenReturn(null);

        // Act
        Authentication result = jwtAuthenticationFilter.attemptAuthentication(request, response);

        // Assert
        assertNull(result);
        assertEquals(401, response.getStatus());
        assertTrue(response.getContentAsString().contains("User not found")); // Vérifiez que la réponse contient le bon message
    }


    @Test
    void testSuccessfulAuthentication() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        Authentication authResult = mock(Authentication.class);
        User mockUser = new User("testUser", "password", Collections.emptyList());
        when(authResult.getPrincipal()).thenReturn(mockUser);

        when(jwtUtil.createRefreshToken("testUser")).thenReturn("refreshToken");
        when(jwtUtil.generateAccessToken("testUser")).thenReturn("accessToken");

        Utilisateur mockUtilisateur = new Utilisateur();
        mockUtilisateur.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(mockUtilisateur);

        // Act
        jwtAuthenticationFilter.successfulAuthentication(request, response, chain, authResult);

        // Assert
        String jsonResponse = response.getContentAsString();
        assertTrue(jsonResponse.contains("refreshToken"));
        assertTrue(jsonResponse.contains("accessToken"));
        assertTrue(jsonResponse.contains("testUser"));
    }

    @Test
    void testUnsuccessfulAuthentication() throws IOException, ServletException {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException exception = new BadCredentialsException("Invalid credentials");

        // Act
        jwtAuthenticationFilter.unsuccessfulAuthentication(request, response, exception);

        // Assert
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertTrue(response.getContentAsString().contains("Invalid credentials"));
    }

    // Subclass to expose protected methods for testing
    class TestJwtAuthenticationFilter extends JwtAuthenticationFilter {
        public TestJwtAuthenticationFilter(AuthenticationManager authenticationManager, UserService accountService,
                                           JwtUtil jwtUtil, UserRepository userRepo) {
            super(authenticationManager, accountService, jwtUtil, userRepo);
        }

        @Override
        public void successfulAuthentication(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain chain,
                                             Authentication authResult) throws IOException, ServletException {
            super.successfulAuthentication(request, response, chain, authResult);
        }

        @Override
        public void unsuccessfulAuthentication(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response,
                                               AuthenticationException failed) throws IOException, ServletException {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }
}
