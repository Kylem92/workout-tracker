package com.kyle.csvmanager.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.kyle.csvmanager.model.Exercise;
import com.kyle.csvmanager.model.Workout;

@ActiveProfiles("test")
@ContextConfiguration(classes = WorkoutCsvWriter.class)
@SpringBootTest
class WorkoutCsvWriterTest {

    @Autowired
    private WorkoutCsvWriter workoutCsvWriter;

    @MockBean
    private Exercise mockExercise;

    @MockBean
    private Workout mockWorkout;

    @Test
    void testWriteWorkoutData_SingleWorkout() throws IOException {
	// given

	List<Workout> workouts = new ArrayList<>();
	when(mockWorkout.getWeekNumber()).thenReturn(1);
	when(mockWorkout.getDayNumber()).thenReturn(1);
	when(mockWorkout.getUserId()).thenReturn("user1");
	when(mockWorkout.getExercises()).thenReturn(List.of(mockExercise));
	when(mockExercise.toCsvList()).thenReturn(List.of("Squat", "3", "10", "Medium", "100", "2-0-1", "90"));

	workouts.add(mockWorkout);

	// when
	workoutCsvWriter.writeWorkoutData(workouts);

	// then
	assertTrue(Files.exists(Path.of("user1-" + LocalDate.now() + ".csv")));
	List<String> lines = Files.readAllLines(Path.of("user1-" + LocalDate.now() + ".csv"));

	assertNotNull(lines);
	assertTrue(lines.size() > 0);
	assertTrue(lines.contains("Week 1"));
	assertTrue(lines.contains("Day 1"));
	assertTrue(lines.stream().anyMatch(line -> line.contains("Squat")));
    }

    @Test
    void testWriteWorkoutData_MultipleWorkouts() throws IOException {
	// given
	List<Workout> workouts = new ArrayList<>();

	Workout workout1 = mock(Workout.class);
	when(workout1.getWeekNumber()).thenReturn(1);
	when(workout1.getDayNumber()).thenReturn(1);
	when(workout1.getUserId()).thenReturn("user1");
	when(workout1.getExercises()).thenReturn(List.of(mockExercise));

	Workout workout2 = mock(Workout.class);
	when(workout2.getWeekNumber()).thenReturn(1);
	when(workout2.getDayNumber()).thenReturn(2);
	when(workout2.getUserId()).thenReturn("user1");
	when(workout2.getExercises()).thenReturn(List.of(mockExercise));

	when(mockExercise.toCsvList()).thenReturn(List.of("Squat", "3", "10", "Medium", "100", "2-0-1", "90"));

	workouts.add(workout1);
	workouts.add(workout2);

	// when
	workoutCsvWriter.writeWorkoutData(workouts);

	// then
	assertTrue(Files.exists(Path.of("user1-" + LocalDate.now() + ".csv")));
	List<String> lines = Files.readAllLines(Path.of("user1-" + LocalDate.now() + ".csv"));

	assertNotNull(lines);
	assertTrue(lines.size() > 0);
	assertTrue(lines.contains("Week 1"));
	assertTrue(lines.contains("Day 1"));
	assertTrue(lines.contains("Day 2"));
    }

    @Test
    void testWriteWorkoutData_EmptyList() {
	// give
	List<Workout> workouts = new ArrayList<>();

	// when
	assertDoesNotThrow(() -> workoutCsvWriter.writeWorkoutData(workouts));

	// then
    }

}
