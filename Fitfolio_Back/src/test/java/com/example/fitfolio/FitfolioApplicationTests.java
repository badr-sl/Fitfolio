package com.example.fitfolio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FitfolioApplicationTests {


	@Test
	void contextLoads() {
		// Test if the application context loads successfully
		assertTrue(true, "Application context loaded successfully.");
	}
	@Test
	void testMainMethod() {
		// Arrange
		String[] args = {};

		// Act & Assert
		FitfolioApplication.main(args); // Run the main method

		// No exceptions should be thrown; if the context loads, this line is successful.
	}

}
