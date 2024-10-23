package com.kyle.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kyle.user.exceptions.UserCrudException;
import com.kyle.user.model.User;
import com.kyle.user.service.UserService;
import com.kyle.user.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = UserController.class)
class UserControllerTests {

    @Autowired
    UserController userController;

    @MockBean
    UserService userService;

    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void beforeAllTests() {
	mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void beforeEachTest() {
	// prepare test
	mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void contextLoads() {
	assertThat(userController).isNotNull();
    }

    @Test
    void testGetAllUsers_MVC_get() throws Exception {
	// given
	List<User> users = TestHelper.createUsers(3);
	when(userService.getAllUsers()).thenReturn(users);
	// when
	MvcResult actual = mockMvc.perform(get("/user/getall").accept("application/json")).andExpect(status().isOk())
		.andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<User> actualList = mapper.readValue(contentAsString, new TypeReference<List<User>>() {
	});
	verify(userService).getAllUsers();
	assertNotNull(contentAsString);
	assertEquals(3, actualList.size());
	assertEquals("1", actualList.get(0).getUserId());
    }

    @Test
    void testGetAllUsers_MVC_get_empty() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/user/getall").accept("application/json")).andExpect(status().isOk())
		.andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<User> actualList = mapper.readValue(contentAsString, new TypeReference<List<User>>() {
	});
	verify(userService).getAllUsers();
	assertNotNull(contentAsString);
	assertEquals(0, actualList.size());
	assertTrue(actualList.isEmpty());
    }

    @Test
    void testGetAllUsers_MVC_get_failure() throws Exception {
	// given
	when(userService.getAllUsers()).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/user/getall").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).getAllUsers();
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserById_MVC_get() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "", "");
	String id = "userId";
	when(userService.findById(id)).thenReturn(Optional.of(user));
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyid/userId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	verify(userService).findById(id);
	assertNotNull(contentAsString);
	assertEquals(user.getUserId(), actualUser.getUserId());
    }

    @Test
    void testGetUserById_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyid/userId").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findById(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserById_MVC_get_failure() throws Exception {
	// given
	String id = "userId";
	when(userService.findById(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyid/userId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findById(id);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserByUsername_MVC_get() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "");
	String username = "username";
	when(userService.findByUsername(username)).thenReturn(Optional.of(user));
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyusername/username").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	verify(userService).findByUsername(username);
	assertNotNull(contentAsString);
	assertEquals(user.getUserId(), actualUser.getUserId());
	assertEquals(user.getUsername(), actualUser.getUsername());
    }

    @Test
    void testGetUserByUsername_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyusername/username").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findByUsername(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserByUsername_MVC_get_failure() throws Exception {
	// given

	String username = "username";
	when(userService.findByUsername(username)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/user/getbyusername/username").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findByUsername(username);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserByUsernameAndPassword_MVC_get() throws Exception {
	// given
	String username = "username";
	String password = "password";
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));
	// when
	MvcResult actual = mockMvc
		.perform(get("/user/getbyusernameandpassword/username/password").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	verify(userService).findByUsernameAndPassword(username, password);
	assertNotNull(contentAsString);
	assertEquals(user.getUserId(), actualUser.getUserId());
	assertEquals(user.getUsername(), actualUser.getUsername());
	assertEquals(user.getPassword(), actualUser.getPassword());
    }

    @Test
    void testGetUserByUsernameAndPassword_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc
		.perform(get("/user/getbyusernameandpassword/username/password").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findByUsernameAndPassword(anyString(), anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetUserByUsernameAndPassword_MVC_get_failure() throws Exception {
	// given
	String username = "username";
	String password = "password";
	when(userService.findByUsernameAndPassword(username, password))
		.thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(get("/user/getbyusernameandpassword/username/password").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).findByUsernameAndPassword(username, password);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testCreateUser_MVC_post() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.saveUser(user)).thenReturn(user);
	// when
	MvcResult actual = mockMvc.perform(post("/user/create").content(mapper.writeValueAsString(user))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	assertEquals(user, actualUser);
	verify(userService).saveUser(user);

    }

    @Test
    void testCreateUser_MVC_post_username_already_exists() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.saveUser(user)).thenThrow(new UserCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(post("/user/create").content(mapper.writeValueAsString(user))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	assertEquals("TEST_EXCEPTION", actualUser.getErrorsAndWarnings().get(0));
	verify(userService).saveUser(any());
    }

    @Test
    void testCreateUser_MVC_post_failure() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.saveUser(user)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(post("/user/create").content(mapper.writeValueAsString(user))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(userService).saveUser(user);
    }

    @Test
    void testUpdateUser_MVC_post() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.updateUser(user)).thenReturn(user);
	// when
	MvcResult actual = mockMvc.perform(put("/user/update").content(mapper.writeValueAsString(user))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	assertEquals(user, actualUser);
	verify(userService).updateUser(user);

    }

    @Test
    void testUpdateUser_MVC_post_username_already_exists() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.updateUser(user)).thenThrow(new UserCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/user/update").content(mapper.writeValueAsString(user))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	assertEquals("TEST_EXCEPTION", actualUser.getErrorsAndWarnings().get(0));
	verify(userService).updateUser(any());
    }

    @Test
    void testUpdateUser_MVC_post_failure() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "username", "password");
	when(userService.updateUser(user)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(put("/user/update").content(mapper.writeValueAsString(user))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(userService).updateUser(user);
    }

    @Test
    void testDeleteUserById_MVC_get() throws Exception {
	// given
	User user = TestHelper.createUser("userId", "", "");
	String id = "userId";
	when(userService.deleteUser(id)).thenReturn(user);
	// when
	MvcResult actual = mockMvc.perform(put("/user/deletebyid/userId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	verify(userService).deleteUser(id);
	assertNotNull(contentAsString);
	assertEquals(user.getUserId(), actualUser.getUserId());
    }

    @Test
    void testDeleteUserById_MVC_get_not_found() throws Exception {
	// given
	when(userService.deleteUser(anyString()))
		.thenThrow(new UserCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/user/deletebyid/userId").accept("application/json"))
		.andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	User actualUser = mapper.readValue(contentAsString, User.class);
	assertEquals("TEST_EXCEPTION", actualUser.getErrorsAndWarnings().get(0));
	verify(userService).deleteUser(anyString());
    }

    @Test
    void testDeleteUserById_MVC_get_failure() throws Exception {
	// given
	String id = "userId";
	when(userService.deleteUser(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(put("/user/deletebyid/userId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(userService).deleteUser(id);
	assertThat(contentAsString).isBlank();
    }

}
