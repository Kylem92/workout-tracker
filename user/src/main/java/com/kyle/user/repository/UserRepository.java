package com.kyle.user.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kyle.sharedmodels.models.User;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
	
	List<User> findAll();
	User findOneByUserId(String id);
	User findOneByUsername(String userName);
	User findOneByUsernameAndPassword(String userName, String password);
}
