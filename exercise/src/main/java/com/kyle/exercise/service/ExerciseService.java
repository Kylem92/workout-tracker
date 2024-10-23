package com.kyle.exercise.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kyle.commonutils.StringUtilsHelper;
import com.kyle.exercise.enums.Status;
import com.kyle.exercise.exceptions.ExerciseCrudException;
import com.kyle.exercise.model.Exercise;
import com.kyle.exercise.repository.ExerciseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepo;

    public List<Exercise> getAllExercises() {
	return exerciseRepo.findAll();
    }

    public Optional<Exercise> findById(String id) {
	return exerciseRepo.findOneByExerciseId(id);
    }

    public Optional<Exercise> findByName(String name) {
	return exerciseRepo.findOneByName(name);
    }

    public Optional<Exercise> findByType(String type) {
	return exerciseRepo.findOneByType(type);
    }

    public Exercise saveExercise(Exercise exercise) {
	Optional<Exercise> existing = exerciseRepo.findOneByName(exercise.getName());
	if (existing.isEmpty()) {
	    exercise.setExerciseId(StringUtilsHelper.generateUuid(12));
	    exercise.setDateCreated(LocalDate.now());
	    exercise.setStatus(Status.ACTIVE);
	    return exerciseRepo.save(exercise);
	} else {
	    throw new ExerciseCrudException("Exercise with name: " + exercise.getName() + " already exists!");
	}

    }

    public Exercise updateExercise(Exercise exercise) {
	Optional<Exercise> existing = exerciseRepo.findOneByExerciseId(exercise.getExerciseId());
	if (existing.isPresent()) {
	    return exerciseRepo.save(exercise);
	} else {
	    throw new ExerciseCrudException("Exercise with id: " + exercise.getExerciseId() + " does not exist!");
	}
    }

    public Exercise deleteExercise(String exerciseId) {
	Optional<Exercise> existing = exerciseRepo.findOneByExerciseId(exerciseId);
	if (existing.isPresent()) {
	    Exercise exercise = existing.get();
	    exercise.setStatus(Status.INACTIVE);
	    return exerciseRepo.save(exercise);
	} else {
	    throw new ExerciseCrudException("Exercise with id: " + exerciseId + " does not exist!");
	}
    }
}
