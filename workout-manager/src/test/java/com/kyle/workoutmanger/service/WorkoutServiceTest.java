package com.kyle.workoutmanger.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.kyle.workoutmanager.enums.Status;
import com.kyle.workoutmanager.exceptions.WorkoutCrudException;
import com.kyle.workoutmanager.model.Workout;
import com.kyle.workoutmanager.repository.WorkoutRepository;
import com.kyle.workoutmanager.service.WorkoutService;
import com.kyle.workoutmanager.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = WorkoutService.class)
class WorkoutServiceTest {

    @Autowired
    WorkoutService workoutService;

    @MockBean
    WorkoutRepository workoutRepo;

    @BeforeEach
    public void setupTest() {
	// prepare test
    }

    @Test
    void contextLoads() {
	assertThat(workoutService).isNotNull();
    }

    @Test
    void testGetall() {
	// given
	List<Workout> workouts = TestHelper.createWorkouts(3);
	when(workoutRepo.findAll()).thenReturn(workouts);
	// when
	List<Workout> actualWorkouts = workoutService.getAllWorkouts();
	// then
	assertEquals(workouts, actualWorkouts);
    }

    @Test
    void testFindById() {
	// given
	String id = "test";
	Workout workout = TestHelper.createWorkout("test");
	when(workoutRepo.findOneByWorkoutId(id)).thenReturn(Optional.of(workout));
	// when
	Optional<Workout> actual = workoutService.findById(id);
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	assertNotNull(actual.get());
    }

    @Test
    void testFindById_not_found() {
	// given
	String id = "test";
	// when
	Optional<Workout> actual = workoutService.findById(id);
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testSaveworkout() {
	// given
	Workout workout = TestHelper.createWorkout("test");
	when(workoutRepo.save(workout)).thenReturn(workout);
	// when
	Workout actual = workoutService.saveWorkout(workout);
	// then
	verify(workoutRepo).save(workout);
	assertNotNull(actual);
	assertEquals(workout, actual);
    }

    @Test
    void testUpdateworkout() {
	// given
	String id = "test";
	Workout workout = TestHelper.createWorkout("test");
	when(workoutRepo.findOneByWorkoutId(id)).thenReturn(Optional.of(workout));
	when(workoutRepo.save(workout)).thenReturn(workout);
	// when
	Workout actual = workoutService.updateWorkout(workout);
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	verify(workoutRepo).save(workout);
	assertNotNull(actual);
	assertEquals(workout, actual);
    }

    @Test
    void testUpdateworkout_doesnt_exist() {
	// given
	String id = "test";
	Workout workout = TestHelper.createWorkout("test");
	when(workoutRepo.findOneByWorkoutId(id)).thenReturn(Optional.empty());
	// when
	WorkoutCrudException actual = assertThrows(WorkoutCrudException.class,
		() -> workoutService.updateWorkout(workout));
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	verify(workoutRepo, never()).save(workout);
	assertThat(actual.getMessage()).isEqualTo("Workout with id: test does not exist!");
    }

    @Test
    void testDeleteworkout() {
	// given
	String id = "test";
	Workout workout = TestHelper.createWorkout("test");
	workout.setStatus(Status.ACTIVE);
	when(workoutRepo.findOneByWorkoutId(id)).thenReturn(Optional.of(workout));
	when(workoutRepo.save(workout)).thenReturn(workout);
	// when
	Workout actual = workoutService.deleteWorkout(id);
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	verify(workoutRepo).save(workout);
	assertNotNull(actual);
	assertEquals(workout, actual);
	assertEquals(Status.INACTIVE, actual.getStatus());
    }

    @Test
    void testDeleteworkout_doesnt_exist() {
	// given
	String id = "test";
	Workout workout = TestHelper.createWorkout("test");
	workout.setStatus(Status.ACTIVE);
	when(workoutRepo.findOneByWorkoutId(id)).thenReturn(Optional.empty());
	// when
	WorkoutCrudException actual = assertThrows(WorkoutCrudException.class, () -> workoutService.deleteWorkout(id));
	// then
	verify(workoutRepo).findOneByWorkoutId(id);
	verify(workoutRepo, never()).save(workout);
	assertThat(actual.getMessage()).isEqualTo("Workout with id: test does not exist!");
	assertEquals(Status.ACTIVE, workout.getStatus());
    }
}
