package com.kyle.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kyle.sharedmodels.models.User;
import com.kyle.user.service.UserService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = UserController.class)
public class UserControllerTests {

	@Autowired
	UserController userController;

	@MockBean
	UserService userService;

	private MockMvc mockMvc;

	private ObjectMapper mapper;

	@BeforeEach
	public void setupTest() {
		// prepare test
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		mapper = new ObjectMapper();
	}
	
	@Test
	void contextLoads() throws Exception {
		assertThat(userController).isNotNull();
	}

	@Test
	void testGetAllUsers() {
		// Given
		List<User> users = createUsers(3);
		when(userService.getAllUsers()).thenReturn(users);
		// When
		List<User> actual = userController.getAllUsers().getBody();
		// Then
		verify(userService).getAllUsers();
		assertEquals(actual, users);
	}
	
	@Test
	  void testGetAllUsers_MVC_get() throws Exception {
	    // given
	    // when
	    MvcResult actual = mockMvc.perform(get("/user/getall")
	        .accept("application/json"))
	        .andExpect(status().isOk())
	        .andReturn();
	    // then
	    String contentAsString = actual.getResponse().getContentAsString();
	    assertNotNull(contentAsString);
	  }
	
	@Test
	void testGetUserById() {
		// Given
		User users = createUser("userId", "", "");
		String id = "userId";
		when(userService.findById(id)).thenReturn(users);
		// When
		User actual = userController.findById(id).getBody();
		// Then
		verify(userService).findById(id);
		assertEquals(actual, users);
	}
	
	  @Test
	  void testGetUserById_MVC_get() throws Exception {
	    // given
		when(userService.findById(anyString())).thenReturn(createUser("userId", "", ""));
	    // when
	    MvcResult actual = mockMvc.perform(get("/user/getbyid/userId")
	        .accept("application/json"))
	        .andExpect(status().isOk())
	        .andReturn();
	    // then
	    String contentAsString = actual.getResponse().getContentAsString();
	    assertNotNull(contentAsString);
	  }
	  
	  @Test
		void testGetUserByUsername() {
			// Given
			User users = createUser("userId", "username", "");
			String username = "username";
			when(userService.findByUsername(username)).thenReturn(users);
			// When
			User actual = userController.findByUsername(username).getBody();
			// Then
			verify(userService).findByUsername(username);
			assertEquals(actual, users);
		}
		
		  @Test
		  void testGetUserByUsername_MVC_get() throws Exception {
		    // given
			when(userService.findByUsername(anyString())).thenReturn(createUser("userId", "username", ""));
		    // when
		    MvcResult actual = mockMvc.perform(get("/user/getbyusername/username")
		        .accept("application/json"))
		        .andExpect(status().isOk())
		        .andReturn();
		    // then
		    String contentAsString = actual.getResponse().getContentAsString();
		    assertNotNull(contentAsString);
		  }

	public static List<User> createUsers(int i) {
		List<User> users = new ArrayList<>();
		for (int j = 1; j <= i; j++) {
			User user = createUser(String.valueOf(j), "username", "password");
			users.add(user);
		}
		return users;
	}

	public static User createUser(String userId, String username, String password) {
		User user = new User();
		user.setUserId(userId);
		user.setUsername(username);
		user.setPassword(password);
		user.setDateCreated(LocalDate.now());
		return user;
	}
}
