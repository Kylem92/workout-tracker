package com.kyle.user.testutils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.kyle.user.model.User;

public class TestUtils {

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
