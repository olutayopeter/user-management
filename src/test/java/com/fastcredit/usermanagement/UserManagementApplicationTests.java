package com.fastcredit.usermanagement;

import com.fastcredit.usermanagement.dto.request.AuthenticateRequest;
import com.fastcredit.usermanagement.dto.request.UserProfileUpdateRequest;
import com.fastcredit.usermanagement.dto.request.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("stage")
class UserManagementApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testUserRegistration() throws Exception {
		UserRegistrationRequest request = new UserRegistrationRequest();
		request.setUsername("Goodness2027");
		request.setEmail("goodness@2027@example.com");
		request.setPassword("Goodness20271@");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(CustomResultMatchers.jsonContentEquals("{\"status\": \"success\",\"message\": \"User registered successfully\"}"));
				//.andExpect(MockMvcResultMatchers.content().string("User registered successfully"));
	}

	@Test
	public void testUserAuthentication() throws Exception {
		AuthenticateRequest request = new AuthenticateRequest();
		request.setUsernameOrEmail("Goodness2027");
		request.setPassword("Goodness20271@");

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/authenticate-user")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk());
				//.andExpect(MockMvcResultMatchers.content().string("Add expected token here "));
	}

	@Test
	public void testUserProfileRetrieval() throws Exception {
		// Mock authentication token (replace with a valid token)
		String authToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJHb29kbmVzczIwMjciLCJpYXQiOjE2OTQ2Nzc2OTgsImV4cCI6MjA1NDY3NzY5OH0.BJmBKl_e7NGdyk9N5o5CZXnT6oJ6QzEssfGnSff9o9NVwGOUut5lBEUkVPRCvW6TLReVBWWscI3Zl6ZpjLRNQA";

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/profile")
						.header("Authorization", "Bearer " + authToken)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User profile retrieved successfully"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Goodness2027"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("Goodness20271@"));
	}

	@Test
	public void testUserProfileUpdate() throws Exception {
		// Mock authentication token (replace with a valid token)
		String authToken = "your-valid-auth-token";

		UserProfileUpdateRequest request = new UserProfileUpdateRequest();
		request.setEmail("new-email@example.com");
		request.setPassword("NewStrongPassword1@");

		mockMvc.perform(MockMvcRequestBuilders.put("/api/users/profile")
						.header("Authorization", "Bearer " + authToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Profile updated successfully"));
	}

}