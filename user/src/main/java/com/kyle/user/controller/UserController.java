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

import com.kyle.sharedmodels.models.User;
import com.kyle.user.exceptions.ExerciseCrudException;
import com.kyle.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/getall", produces = "application/json")
	public ResponseEntity<List<com.kyle.sharedmodels.models.User>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping(value = "/getbyid/{userId}", produces = "application/json")
	public ResponseEntity<User> findById(@PathVariable String userId) {
		return new ResponseEntity<>(userService.findById(userId), HttpStatus.OK);
	}

	@GetMapping(value = "/getbyusername/{username}", produces = "application/json")
	public ResponseEntity<User> findByUsername(@PathVariable String username) {
		return new ResponseEntity<>(userService.findByUsername(username), HttpStatus.OK);
	}

	@GetMapping(value = "/findbyusernameandpassword/{username}/{password}", produces = "application/json")
	public ResponseEntity<User> findByUsernameAndPassword(@PathVariable String username,
			@PathVariable String password) {
		return new ResponseEntity<>(userService.findByUsernameAndPassword(username, password), HttpStatus.OK);
	}

	@PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		try {
			userService.saveUser(user);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
		} catch (ExerciseCrudException e) {
			log.error(e.getMessage());
			user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(user);
		}
	}

	@PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		try {
			userService.updateUser(user);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(user);
		} catch (ExerciseCrudException e) {
			log.error(e.getMessage());
			user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(user);
		}
	}

	@PutMapping(value = "/deletebyid/{userId}", produces = "application/json", consumes = "application/json")
	public ResponseEntity<User> updateUser(@PathVariable String userId) {
		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deleteUser(userId));
		} catch (ExerciseCrudException e) {
			log.error(e.getMessage());
			User user =  new User();
			user.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(user);
		}
	}
}
