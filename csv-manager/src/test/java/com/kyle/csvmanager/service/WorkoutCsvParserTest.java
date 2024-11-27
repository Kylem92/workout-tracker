package com.kyle.csvmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.multipart.MultipartFile;

import com.kyle.csvmanager.model.Exercise;
import com.kyle.csvmanager.model.Workout;

@ActiveProfiles("test")
@ContextConfiguration(classes = WorkoutCsvParser.class)
@SpringBootTest
class WorkoutCsvParserTest {

    @MockBean
    private MultipartFile mockFile;

    @Autowired
    private WorkoutCsvParser parser;

    @Test
    void parseWorkoutFile_ValidContent_ShouldParseCorrectly() throws IOException {
	// given
	String csvContent = "Week 1\n" + "Day 1 2007-01-02\n" + "Exercise,Sets,Reps,Intensity,Load,Tempo,Rest\n"
		+ "Squat,3,5,High,100.5,2-0-1,90\n" + "Bench Press,3,5,Medium,80.0,2-1-1,60\n" + "\n" + "Week 1\n"
		+ "Day 2 2007-01-03\n" + "Exercise,Sets,Reps,Intensity,Load,Tempo,Rest\n"
		+ "Deadlift,4,3,High,120.5,2-1-1,120\n";

	// Mock the file input stream
	InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
	when(mockFile.getInputStream()).thenReturn(inputStream);

	// when
	List<Workout> workouts = parser.parseWorkoutFile(mockFile);

	// then
	assertNotNull(workouts);
	assertEquals(2, workouts.size());
	Workout day1 = workouts.get(0);
	assertEquals(1, day1.getWeekNumber());
	assertEquals(1, day1.getDayNumber());
	assertEquals(2, day1.getExercises().size());
	Exercise squat = day1.getExercises().get(0);
	assertEquals("Squat", squat.getName());
	assertEquals(3, squat.getSets());
	assertEquals(5, squat.getReps());
	assertEquals("High", squat.getIntensity());
	assertEquals(100.5, squat.getLoad());
	assertEquals("2-0-1", squat.getTempo());
	assertEquals(90, squat.getRest());
	Workout day2 = workouts.get(1);
	assertEquals(1, day2.getWeekNumber());
	assertEquals(2, day2.getDayNumber());
	assertEquals(1, day2.getExercises().size());
    }

    @Test
    void parseWorkoutFile_EmptyFile_ShouldReturnEmptyList() throws IOException {
	// then
	String csvContent = "";
	InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
	when(mockFile.getInputStream()).thenReturn(inputStream);

	// when
	List<Workout> workouts = parser.parseWorkoutFile(mockFile);

	// then
	assertNotNull(workouts);
	assertTrue(workouts.isEmpty());
    }

    @Test
    void parseWorkoutFile_InvalidNumbers_ShouldUseDefaultValues() throws IOException {
	// given
	String csvContent = "Week 1\n" + "Day 1 2007-01-02\n" + "Exercise,Sets,Reps,Intensity,Load,Tempo,Rest\n"
		+ "Squat,invalid,abc,High,invalid,2-0-1,xyz\n";

	InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
	when(mockFile.getInputStream()).thenReturn(inputStream);

	// when
	List<Workout> workouts = parser.parseWorkoutFile(mockFile);

	// then
	assertNotNull(workouts);
	assertEquals(1, workouts.size());

	Exercise exercise = workouts.get(0).getExercises().get(0);
	assertEquals(0, exercise.getSets());
	assertEquals(0, exercise.getReps());
	assertEquals(0.0, exercise.getLoad());
	assertEquals(0, exercise.getRest());
    }

    @Test
    void parseWorkoutFile_MissingValues_ShouldUseDefaultValues() throws IOException {
	// given
	String csvContent = "Week 1\n" + "Day 1 2007-01-02\n" + "Exercise,Sets,Reps,Intensity,Load,Tempo,Rest\n"
		+ "Squat,,,,,,\n";

	InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());
	when(mockFile.getInputStream()).thenReturn(inputStream);

	// when
	List<Workout> workouts = parser.parseWorkoutFile(mockFile);

	// then
	assertNotNull(workouts);
	assertEquals(1, workouts.size());

	Exercise exercise = workouts.get(0).getExercises().get(0);
	assertEquals("Squat", exercise.getName());
	assertEquals(0, exercise.getSets());
	assertEquals(0, exercise.getReps());
	assertEquals(0.0, exercise.getLoad());
	assertNull(exercise.getIntensity());
	assertNull(exercise.getTempo());
	assertEquals(0, exercise.getRest());
    }

    @Test
    void parseWorkoutFile_IOException_ShouldPropagateException() throws IOException {
	// given
	when(mockFile.getInputStream()).thenThrow(new IOException("Test exception"));

	// when
	assertThrows(IOException.class, () -> parser.parseWorkoutFile(mockFile));
    }
}
