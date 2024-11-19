package com.kyle.csvmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kyle.commonutils.ResponseHelper;
import com.kyle.csvmanager.model.Workout;
import com.kyle.csvmanager.service.WorkoutCsvParser;
import com.kyle.csvmanager.service.WorkoutCsvWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/csvmanager")
@RequiredArgsConstructor
public class CsvManagerController {

    private final WorkoutCsvParser workoutParser;
    private final WorkoutCsvWriter workoutCsvWriter;

    @PostMapping(value = "/upload")
    public ResponseEntity<List<Workout>> uploadWorkouts(@RequestParam("workoutcsv") MultipartFile file) {
	try {
	    List<Workout> workouts = workoutParser.parseWorkoutFile(file);
	    workouts.stream().forEach(obj -> obj.setUserId(file.getOriginalFilename()));
	    return new ResponseEntity<>(workouts, HttpStatus.OK);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }

    @PostMapping(value = "/toworkoutcsv")
    public ResponseEntity<List<Workout>> convertWorkoutsToCsv(@RequestBody List<Workout> workouts) {
	try {
	    workoutCsvWriter.writeWorkoutData(workouts);
	    return new ResponseEntity<>(workouts, HttpStatus.OK);
	} catch (Exception e) {
	    log.error(e.getMessage());
	    return ResponseHelper.getResponseEntityWithError(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
}
