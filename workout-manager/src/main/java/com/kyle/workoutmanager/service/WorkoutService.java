package com.kyle.workoutmanager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kyle.commonutils.CustomStringUtils;
import com.kyle.commonutils.JsonUtils;
import com.kyle.workoutmanager.enums.Status;
import com.kyle.workoutmanager.exceptions.WorkoutCrudException;
import com.kyle.workoutmanager.model.Workout;
import com.kyle.workoutmanager.producer.KafkaProducer;
import com.kyle.workoutmanager.repository.WorkoutRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    private final KafkaProducer kafkaProducer;

    public List<Workout> getAllWorkouts() {
	return workoutRepository.findAll();
    }

    public Optional<Workout> findById(String id) {
	return workoutRepository.findOneByWorkoutId(id);
    }

    public Workout saveWorkout(Workout workout) {
	workout.setWorkoutId(CustomStringUtils.generateUuid(12));
	workout.setDateCreated(LocalDate.now());
	workout.setStatus(Status.ACTIVE);
	kafkaProducer.sendMessage(JsonUtils.objectToJSON(workout, false));
	return workoutRepository.save(workout);
    }

    public Workout updateWorkout(Workout workout) {
	Optional<Workout> existing = workoutRepository.findOneByWorkoutId(workout.getWorkoutId());
	if (existing.isPresent()) {
	    kafkaProducer.sendMessage(JsonUtils.objectToJSON(workout, false));
	    return workoutRepository.save(workout);
	} else {
	    throw new WorkoutCrudException("Workout with id: " + workout.getWorkoutId() + " does not exist!");
	}
    }

    public Workout deleteWorkout(String workoutId) {
	Optional<Workout> existing = workoutRepository.findOneByWorkoutId(workoutId);
	if (existing.isPresent()) {
	    Workout existingWorkout = existing.get();
	    existingWorkout.setStatus(Status.INACTIVE);
	    return workoutRepository.save(existingWorkout);
	} else {
	    throw new WorkoutCrudException("Workout with id: " + workoutId + " does not exist!");
	}
    }

}
