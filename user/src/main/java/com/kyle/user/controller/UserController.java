package com.kyle.user.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyle.user.exceptions.UserCrudException;
import com.kyle.user.model.User;
import com.kyle.user.service.UserService;
import com.kyle.user.utils.ResponseHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/getall", produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
	try {
	    log.info("Getting all users");
	    List<User> users = userService.getAllUsers();
	    return ResponseHelper.getResponseEntityOK(users);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyid/{userId}", produces = "application/json")
    public ResponseEntity<User> getById(@PathVariable String userId) {
	try {
	    log.info("Getting user with ID: {}", userId);
	    return userService.findById(userId).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyusername/{username}", produces = "application/json")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
	try {
	    log.info("Getting user with username: {}", username);
	    return userService.findByUsername(username).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyusernameandpassword/{username}/{password}", produces = "application/json")
    public ResponseEntity<User> getByUsernameAndPassword(@PathVariable String username, @PathVariable String password) {
	try {
	    log.info("Getting user with username: {} and password: {}", username, password);
	    return userService.findByUsernameAndPassword(username, password).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<User> createUser(@RequestBody User user) {
	try {
	    return ResponseHelper.getResponseEntityOK(userService.saveUser(user));
	} catch (UserCrudException e) {
	    log.error(e.getMessage());
	    user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(user, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
	try {
	    return ResponseHelper.getResponseEntityOK(userService.updateUser(user));
	} catch (UserCrudException e) {
	    log.error(e.getMessage());
	    user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(user, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/deletebyid/{userId}", produces = "application/json")
    public ResponseEntity<User> deleteUser(@PathVariable String userId) {
	try {
	    return ResponseHelper.getResponseEntityOK(userService.deleteUser(userId));
	} catch (UserCrudException e) {
	    log.error(e.getMessage());
	    User user = new User();
	    user.setUserId(userId);
	    user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(user, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
