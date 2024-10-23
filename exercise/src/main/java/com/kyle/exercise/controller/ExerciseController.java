package com.kyle.exercise.controller;

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

import com.kyle.commonutils.ResponseHelper;
import com.kyle.exercise.exceptions.ExerciseCrudException;
import com.kyle.exercise.model.Exercise;
import com.kyle.exercise.service.ExerciseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping(value = "/getall", produces = "application/json")
    public ResponseEntity<List<Exercise>> getAllExercises() {
	try {
	    log.info("Getting all exercises");
	    return ResponseHelper.getResponseEntityOK(exerciseService.getAllExercises());
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyid/{exerciseId}", produces = "application/json")
    public ResponseEntity<Exercise> findById(@PathVariable String exerciseId) {
	try {
	    log.info("Getting exercise with ID: {}", exerciseId);
	    return exerciseService.findById(exerciseId).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyname/{name}", produces = "application/json")
    public ResponseEntity<Exercise> findByName(@PathVariable String name) {
	try {
	    log.info("Getting exercise with name: {}", name);
	    return exerciseService.findByName(name).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/findbytype/{type}", produces = "application/json")
    public ResponseEntity<Exercise> findByType(@PathVariable String type) {
	try {
	    log.info("Getting exercise with type: {}", type);
	    return exerciseService.findByType(type).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Exercise> saveExercise(@RequestBody Exercise exercise) {
	try {
	    log.info("Creating exercise with name: {}", exercise.getName());
	    return ResponseHelper.getResponseEntityOK(exerciseService.saveExercise(exercise));
	} catch (ExerciseCrudException e) {
	    log.error(e.getMessage());
	    exercise.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(exercise, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Exercise> updateExercise(@RequestBody Exercise exercise) {
	try {
	    log.info("Updating exercise with exerciseId: {}", exercise.getExerciseId());
	    return ResponseHelper.getResponseEntityOK(exerciseService.updateExercise(exercise));
	} catch (ExerciseCrudException e) {
	    log.error(e.getMessage());
	    exercise.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(exercise, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/deletebyid/{exerciseId}", produces = "application/json")
    public ResponseEntity<Exercise> deleteExercise(@PathVariable String exerciseId) {
	try {
	    log.info("Deleting exercise with exerciseId: {}", exerciseId);
	    return ResponseHelper.getResponseEntityOK(exerciseService.deleteExercise(exerciseId));
	} catch (ExerciseCrudException e) {
	    log.error(e.getMessage());
	    Exercise exercise = new Exercise();
	    exercise.setExerciseId(exerciseId);
	    exercise.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(exercise, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
