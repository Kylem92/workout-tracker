package com.kyle.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kyle.user.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAll();

    Optional<User> findOneByUserId(String id);

    Optional<User> findOneByUsername(String userName);

    Optional<User> findOneByUsernameAndPassword(String userName, String password);
}
