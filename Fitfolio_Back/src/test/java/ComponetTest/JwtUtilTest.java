package ComponetTest;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fitfolio.component.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateAccessToken() {
        // Arrange
        String subject = "testUser";

        // Act
        String token = jwtUtil.generateAccessToken(subject);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testCreateRefreshToken() {
        // Arrange
        String subject = "testUser";

        // Act
        String refreshToken = jwtUtil.createRefreshToken(subject);

        // Assert
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
    }

    @Test
    void testVerifySignatureValidToken() {
        // Arrange
        String subject = "testUser";
        String token = jwtUtil.generateAccessToken(subject);

        // Act
        DecodedJWT decodedJWT = jwtUtil.verifySignature(token);

        // Assert
        assertNotNull(decodedJWT);
        assertEquals(subject, decodedJWT.getSubject());
    }

    @Test
    void testVerifySignatureInvalidToken() {
        // Arrange
        String invalidToken = "invalidToken";

        // Act
        DecodedJWT decodedJWT = jwtUtil.verifySignature(invalidToken);

        // Assert
        assertNull(decodedJWT);
    }

    @Test
    void testGetRoles() {
        // Arrange
        String subject = "testUser";
        String token = jwtUtil.generateAccessToken(subject);

        DecodedJWT decodedJWT = jwtUtil.verifySignature(token);
        assertNotNull(decodedJWT);

        // Act
        String[] roles = jwtUtil.getRoles(decodedJWT);

        // Assert
        assertNull(roles); // Roles are not included in the token by default
    }

    @Test
    void testGetUsername() {
        // Arrange
        String subject = "testUser";
        String token = jwtUtil.generateAccessToken(subject);

        DecodedJWT decodedJWT = jwtUtil.verifySignature(token);
        assertNotNull(decodedJWT);

        // Act
        String username = jwtUtil.getUsername(decodedJWT);

        // Assert
        assertEquals(subject, username);
    }
}
