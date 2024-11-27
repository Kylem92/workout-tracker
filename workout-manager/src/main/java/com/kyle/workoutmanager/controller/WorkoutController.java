package com.kyle.workoutmanager.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyle.commonutils.ResponseHelper;
import com.kyle.workoutmanager.exceptions.WorkoutCrudException;
import com.kyle.workoutmanager.model.Workout;
import com.kyle.workoutmanager.service.WorkoutService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/workout")
@RequiredArgsConstructor
public class WorkoutController {

    public static final String X_USER_ID_HEADER_KEY = "x-userid-id";

    private final WorkoutService workoutService;

    @GetMapping(value = "/getall", produces = "application/json")
    public ResponseEntity<List<Workout>> getAllWorkout() {
	try {
	    log.info("Getting all workouts");
	    return ResponseHelper.getResponseEntityOK(workoutService.getAllWorkouts());
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @GetMapping(value = "/getbyid/{workoutId}", produces = "application/json")
    public ResponseEntity<Workout> findById(@PathVariable String workoutId) {
	try {
	    log.info("Getting workout with ID: {}", workoutId);
	    return workoutService.findById(workoutId).map(ResponseHelper::getResponseEntityOK)
		    .orElse(ResponseHelper.getResponseEntityWithError(HttpStatus.NOT_FOUND));
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<Workout>> saveWorkout(
	    @RequestHeader(value = X_USER_ID_HEADER_KEY, required = false) String userId,
	    @RequestBody List<Workout> workouts) {
	try {
	    log.info("Creating workout with userId: {}", userId);
	    workoutService.saveWorkouts(workouts);
	    return ResponseHelper.getResponseEntityOK(workouts);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/update", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Workout> updateWorkout(@RequestBody Workout workout) {
	try {
	    log.info("Updating workout with workoutId: {}", workout.getWorkoutId());
	    return ResponseHelper.getResponseEntityOK(workoutService.updateWorkout(workout));
	} catch (WorkoutCrudException e) {
	    log.error(e.getMessage());
	    workout.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(workout, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PutMapping(value = "/deletebyid/{workoutId}", produces = "application/json")
    public ResponseEntity<Workout> deleteWorkout(@PathVariable String workoutId) {
	try {
	    log.info("Deleting workout with workoutId: {}", workoutId);
	    return ResponseHelper.getResponseEntityOK(workoutService.deleteWorkout(workoutId));
	} catch (WorkoutCrudException e) {
	    log.error(e.getMessage());
	    Workout workout = new Workout();
	    workout.setWorkoutId(workoutId);
	    workout.setErrorsAndWarnings(Arrays.asList(e.getMessage()));
	    return ResponseHelper.getResponseEntityWithError(workout, HttpStatus.FORBIDDEN);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
