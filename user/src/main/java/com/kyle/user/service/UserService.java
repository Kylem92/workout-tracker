package com.kyle.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kyle.commonutils.CustomStringUtils;
import com.kyle.user.enums.Status;
import com.kyle.user.exceptions.UserCrudException;
import com.kyle.user.model.User;
import com.kyle.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public List<User> getAllUsers() {
	return userRepo.findAll();
    }

    public Optional<User> findById(String id) {
	return userRepo.findOneByUserId(id);
    }

    public Optional<User> findByUsername(String username) {
	return userRepo.findOneByUsername(username);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
	return userRepo.findOneByUsernameAndPassword(username, password);
    }

    public User saveUser(User user) {
	Optional<User> existing = userRepo.findOneByUsername(user.getUsername());
	if (existing.isEmpty()) {
	    user.setUserId(CustomStringUtils.generateUuid(12));
	    user.setDateCreated(LocalDate.now());
	    user.setPassword(CustomStringUtils.hashValueIfNeeded(user.getPassword()));
	    user.setStatus(Status.ACTIVE);
	    return userRepo.save(user);
	} else {
	    throw new UserCrudException("User with username: " + user.getUsername() + " already exists!");
	}
    }

    public User updateUser(User user) {
	Optional<User> userByUserId = userRepo.findOneByUserId(user.getUserId());
	if (userByUserId.isPresent()) {
	    return userRepo.save(user);
	} else {
	    throw new UserCrudException("User with id: " + user.getUserId() + " does not exist!");
	}
    }

    public User deleteUser(String userId) {
	Optional<User> userByUserId = userRepo.findOneByUserId(userId);
	if (userByUserId.isPresent()) {
	    User user = userByUserId.get();
	    user.setStatus(Status.INACTIVE);
	    return userRepo.save(user);
	} else {
	    throw new UserCrudException("User with id: " + userId + " does not exist!");
	}
    }
}
