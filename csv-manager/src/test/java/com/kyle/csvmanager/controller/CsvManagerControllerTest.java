package com.kyle.csvmanager.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.kyle.csvmanager.model.Workout;
import com.kyle.csvmanager.service.WorkoutCsvParser;
import com.kyle.csvmanager.service.WorkoutCsvWriter;

@ActiveProfiles("test")
@ContextConfiguration(classes = CsvManagerController.class)
@SpringBootTest
class CsvManagerControllerTest {

    @MockBean
    private WorkoutCsvParser workoutParser;

    @MockBean
    private WorkoutCsvWriter workoutCsvWriter;

    @Autowired
    private CsvManagerController csvManagerController;

    private MockMultipartFile mockFile;
    private List<Workout> sampleWorkouts;

    @BeforeEach
    void setUp() {
	mockFile = new MockMultipartFile("workoutcsv", "test.csv", "text/csv", "sample,data,content".getBytes());
	sampleWorkouts = Arrays.asList(new Workout(1, 2), new Workout(2, 1));
    }

    @Test
    void uploadWorkouts_Success() throws Exception {
	// given
	when(workoutParser.parseWorkoutFile(mockFile)).thenReturn(sampleWorkouts);

	// when
	ResponseEntity<List<Workout>> response = csvManagerController.uploadWorkouts(mockFile);

	// then
	assertNotNull(response);
	assertEquals(HttpStatus.OK, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(2, response.getBody().size());
	response.getBody().forEach(workout -> assertEquals(mockFile.getOriginalFilename(), workout.getUserId()));
	verify(workoutParser).parseWorkoutFile(mockFile);
    }

    @Test
    void uploadWorkouts_ExceptionHandling() throws Exception {
	// given
	when(workoutParser.parseWorkoutFile(mockFile)).thenThrow(new RuntimeException("Parse error"));

	// when
	ResponseEntity<List<Workout>> response = csvManagerController.uploadWorkouts(mockFile);

	// then
	assertNotNull(response);
	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	verify(workoutParser).parseWorkoutFile(mockFile);
    }

    @Test
    void convertWorkoutsToCsv_Success() throws IOException {
	// given
	doNothing().when(workoutCsvWriter).writeWorkoutData(sampleWorkouts);

	// when
	ResponseEntity<List<Workout>> response = csvManagerController.convertWorkoutsToCsv(sampleWorkouts);

	// then
	assertNotNull(response);
	assertEquals(HttpStatus.OK, response.getStatusCode());
	assertNotNull(response.getBody());
	assertEquals(sampleWorkouts, response.getBody());
	verify(workoutCsvWriter).writeWorkoutData(sampleWorkouts);
    }

    @Test
    void convertWorkoutsToCsv_ExceptionHandling() throws IOException {
	// given
	doThrow(new RuntimeException("Write error")).when(workoutCsvWriter).writeWorkoutData(sampleWorkouts);

	// when
	ResponseEntity<List<Workout>> response = csvManagerController.convertWorkoutsToCsv(sampleWorkouts);

	// then
	assertNotNull(response);
	assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	verify(workoutCsvWriter).writeWorkoutData(sampleWorkouts);
    }
}
