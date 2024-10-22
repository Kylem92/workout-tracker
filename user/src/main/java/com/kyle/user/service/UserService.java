package com.kyle.user.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import com.kyle.user.enums.Status;
import com.kyle.user.exceptions.UserCrudException;
import com.kyle.user.model.User;
import com.kyle.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final char[][] UUID_CHARS = new char[][] { { 'A', 'Z' }, { '0', '9' } };
    private static final RandomStringGenerator builder = new RandomStringGenerator.Builder().withinRange(UUID_CHARS)
	    .build();

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
	    user.setUserId(generateUuid());
	    user.setDateCreated(LocalDate.now());
	    user.setPassword(hashValue(user.getPassword()));
	    user.setStatus(Status.ACTIVE);
	    return userRepo.save(user);
	} else {
	    throw new UserCrudException("User with username: " + user.getUsername() + " already exists!");
	}
    }

    private String hashValue(String value) {
	if (StringUtils.isBlank(value) || value.matches("\\w{64}")) {
	    return value;
	}
	return DigestUtils.sha256Hex(value);
    }

    private static String generateUuid() {
	return builder.generate(12);
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
