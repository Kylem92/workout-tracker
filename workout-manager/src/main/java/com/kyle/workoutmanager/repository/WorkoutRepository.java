package com.kyle.workoutmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kyle.workoutmanager.model.Workout;

@Repository
public interface WorkoutRepository extends MongoRepository<Workout, String> {

    List<Workout> findAll();

    Optional<Workout> findOneByWorkoutId(String id);

}
