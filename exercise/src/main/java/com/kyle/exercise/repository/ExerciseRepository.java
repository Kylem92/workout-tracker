package com.kyle.exercise.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kyle.exercise.model.Exercise;

@Repository
public interface ExerciseRepository extends MongoRepository<Exercise, String> {

    List<Exercise> findAll();

    Optional<Exercise> findOneByExerciseId(String id);

    Optional<Exercise> findOneByName(String name);

    Optional<Exercise> findOneByType(String type);
}
