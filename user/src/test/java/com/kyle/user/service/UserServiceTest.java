package com.kyle.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.kyle.user.enums.Status;
import com.kyle.user.exceptions.UserCrudException;
import com.kyle.user.model.User;
import com.kyle.user.repository.UserRepository;
import com.kyle.user.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = UserService.class)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepo;

    @BeforeEach
    public void setupTest() {
	// prepare test
    }

    @Test
    void contextLoads() {
	assertThat(userService).isNotNull();
    }

    @Test
    void testGetall() {
	// given
	List<User> users = TestHelper.createUsers(3);
	when(userRepo.findAll()).thenReturn(users);
	// when
	List<User> actualUsers = userService.getAllUsers();
	// then
	assertEquals(users, actualUsers);
    }

    @Test
    void testFindById() {
	// given
	String id = "test";
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUserId(id)).thenReturn(Optional.of(user));
	// when
	Optional<User> actual = userService.findById(id);
	// then
	verify(userRepo).findOneByUserId(id);
	assertNotNull(actual.get());
    }

    @Test
    void testFindById_not_found() {
	// given
	String id = "test";
	// when
	Optional<User> actual = userService.findById(id);
	// then
	verify(userRepo).findOneByUserId(id);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByUsername() {
	// given
	String username = "username";
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUsername(username)).thenReturn(Optional.of(user));
	// when
	Optional<User> actual = userService.findByUsername(username);
	// then
	verify(userRepo).findOneByUsername(username);
	assertNotNull(actual.get());
    }

    @Test
    void testFindByUsername_not_found() {
	// given
	String username = "username";
	// when
	Optional<User> actual = userService.findByUsername(username);
	// then
	verify(userRepo).findOneByUsername(username);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByUsernameAndPassword() {
	// given
	String username = "username";
	String password = "password";
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));
	// when
	Optional<User> actual = userService.findByUsernameAndPassword(username, password);
	// then
	verify(userRepo).findOneByUsernameAndPassword(username, password);
	assertNotNull(actual.get());
    }

    @Test
    void testFindByUsernameAndPassword_not_found() {
	// given
	String username = "username";
	String password = "password";
	// when
	Optional<User> actual = userService.findByUsernameAndPassword(username, password);
	// then
	verify(userRepo).findOneByUsernameAndPassword(username, password);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testSaveUser() {
	// given
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.save(user)).thenReturn(user);
	// when
	User actual = userService.saveUser(user);
	// then
	verify(userRepo).findOneByUsername("username");
	verify(userRepo).save(user);
	assertNotNull(actual);
	assertEquals(user, actual);
    }

    @Test
    void testSaveUser_already_exists() {
	// given
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUsername("username")).thenReturn(Optional.of(user));
	// when
	UserCrudException actual = assertThrows(UserCrudException.class, () -> userService.saveUser(user));
	// then
	verify(userRepo).findOneByUsername("username");
	verify(userRepo, never()).save(user);
	assertThat(actual.getMessage()).isEqualTo("User with username: username already exists!");
    }

    @Test
    void testUpdateUser() {
	// given
	String id = "test";
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUserId(id)).thenReturn(Optional.of(user));
	when(userRepo.save(user)).thenReturn(user);
	// when
	User actual = userService.updateUser(user);
	// then
	verify(userRepo).findOneByUserId(id);
	verify(userRepo).save(user);
	assertNotNull(actual);
	assertEquals(user, actual);
    }

    @Test
    void testUpdateUser_doesnt_exist() {
	// given
	String id = "test";
	User user = TestHelper.createUser("test", "username", "password");
	when(userRepo.findOneByUserId(id)).thenReturn(Optional.empty());
	// when
	UserCrudException actual = assertThrows(UserCrudException.class, () -> userService.updateUser(user));
	// then
	verify(userRepo).findOneByUserId(id);
	verify(userRepo, never()).save(user);
	assertThat(actual.getMessage()).isEqualTo("User with id: test does not exist!");
    }

    @Test
    void testDeleteUser() {
	// given
	String id = "test";
	User user = TestHelper.createUser("test", "username", "password");
	user.setStatus(Status.ACTIVE);
	when(userRepo.findOneByUserId(id)).thenReturn(Optional.of(user));
	when(userRepo.save(user)).thenReturn(user);
	// when
	User actual = userService.deleteUser(id);
	// then
	verify(userRepo).findOneByUserId(id);
	verify(userRepo).save(user);
	assertNotNull(actual);
	assertEquals(user, actual);
	assertEquals(Status.INACTIVE, actual.getStatus());
    }

    @Test
    void testDeleteUser_doesnt_exist() {
	// given
	String id = "test";
	User user = TestHelper.createUser("test", "username", "password");
	user.setStatus(Status.ACTIVE);
	when(userRepo.findOneByUserId(id)).thenReturn(Optional.empty());
	// when
	UserCrudException actual = assertThrows(UserCrudException.class, () -> userService.deleteUser(id));
	// then
	verify(userRepo).findOneByUserId(id);
	verify(userRepo, never()).save(user);
	assertThat(actual.getMessage()).isEqualTo("User with id: test does not exist!");
	assertEquals(Status.ACTIVE, user.getStatus());
    }
}
