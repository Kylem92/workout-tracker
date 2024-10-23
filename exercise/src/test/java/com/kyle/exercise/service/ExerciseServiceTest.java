package com.kyle.exercise.service;

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

import com.kyle.exercise.enums.Status;
import com.kyle.exercise.exceptions.ExerciseCrudException;
import com.kyle.exercise.model.Exercise;
import com.kyle.exercise.repository.ExerciseRepository;
import com.kyle.exercise.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = ExerciseService.class)
class ExerciseServiceTest {

    @Autowired
    ExerciseService exerciseService;

    @MockBean
    ExerciseRepository exerciseRepo;

    @BeforeEach
    public void setupTest() {
	// prepare test
    }

    @Test
    void contextLoads() {
	assertThat(exerciseService).isNotNull();
    }

    @Test
    void testGetall() {
	// given
	List<Exercise> exercises = TestHelper.createExercises(3);
	when(exerciseRepo.findAll()).thenReturn(exercises);
	// when
	List<Exercise> actualExercises = exerciseService.getAllExercises();
	// then
	assertEquals(exercises, actualExercises);
    }

    @Test
    void testFindById() {
	// given
	String id = "test";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.findOneByExerciseId(id)).thenReturn(Optional.of(exercise));
	// when
	Optional<Exercise> actual = exerciseService.findById(id);
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	assertNotNull(actual.get());
    }

    @Test
    void testFindById_not_found() {
	// given
	String id = "test";
	// when
	Optional<Exercise> actual = exerciseService.findById(id);
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByExerciseName() {
	// given
	String exerciseName = "exercisename";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.findOneByName(exerciseName)).thenReturn(Optional.of(exercise));
	// when
	Optional<Exercise> actual = exerciseService.findByName(exerciseName);
	// then
	verify(exerciseRepo).findOneByName(exerciseName);
	assertNotNull(actual.get());
    }

    @Test
    void testFindByExerciseName_not_found() {
	// given
	String exerciseName = "exercisename";
	// when
	Optional<Exercise> actual = exerciseService.findByName(exerciseName);
	// then
	verify(exerciseRepo).findOneByName(exerciseName);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testFindByType() {
	// given
	String type = "type";
	Exercise exercise = TestHelper.createExercise("test", "type");
	when(exerciseRepo.findOneByType(type)).thenReturn(Optional.of(exercise));
	// when
	Optional<Exercise> actual = exerciseService.findByType(type);
	// then
	verify(exerciseRepo).findOneByType(type);
	assertNotNull(actual.get());
    }

    @Test
    void testFindByType_not_found() {
	// given
	String type = "type";
	// when
	Optional<Exercise> actual = exerciseService.findByType(type);
	// then
	verify(exerciseRepo).findOneByType(type);
	assertTrue(actual.isEmpty());
    }

    @Test
    void testSaveexercise() {
	// given
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.save(exercise)).thenReturn(exercise);
	// when
	Exercise actual = exerciseService.saveExercise(exercise);
	// then
	verify(exerciseRepo).findOneByName("exercisename");
	verify(exerciseRepo).save(exercise);
	assertNotNull(actual);
	assertEquals(exercise, actual);
    }

    @Test
    void testSaveexercise_already_exists() {
	// given
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.findOneByName("exercisename")).thenReturn(Optional.of(exercise));
	// when
	ExerciseCrudException actual = assertThrows(ExerciseCrudException.class,
		() -> exerciseService.saveExercise(exercise));
	// then
	verify(exerciseRepo).findOneByName("exercisename");
	verify(exerciseRepo, never()).save(exercise);
	assertThat(actual.getMessage()).isEqualTo("Exercise with name: exercisename already exists!");
    }

    @Test
    void testUpdateexercise() {
	// given
	String id = "test";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.findOneByExerciseId(id)).thenReturn(Optional.of(exercise));
	when(exerciseRepo.save(exercise)).thenReturn(exercise);
	// when
	Exercise actual = exerciseService.updateExercise(exercise);
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	verify(exerciseRepo).save(exercise);
	assertNotNull(actual);
	assertEquals(exercise, actual);
    }

    @Test
    void testUpdateexercise_doesnt_exist() {
	// given
	String id = "test";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	when(exerciseRepo.findOneByExerciseId(id)).thenReturn(Optional.empty());
	// when
	ExerciseCrudException actual = assertThrows(ExerciseCrudException.class,
		() -> exerciseService.updateExercise(exercise));
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	verify(exerciseRepo, never()).save(exercise);
	assertThat(actual.getMessage()).isEqualTo("Exercise with id: test does not exist!");
    }

    @Test
    void testDeleteexercise() {
	// given
	String id = "test";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	exercise.setStatus(Status.ACTIVE);
	when(exerciseRepo.findOneByExerciseId(id)).thenReturn(Optional.of(exercise));
	when(exerciseRepo.save(exercise)).thenReturn(exercise);
	// when
	Exercise actual = exerciseService.deleteExercise(id);
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	verify(exerciseRepo).save(exercise);
	assertNotNull(actual);
	assertEquals(exercise, actual);
	assertEquals(Status.INACTIVE, actual.getStatus());
    }

    @Test
    void testDeleteexercise_doesnt_exist() {
	// given
	String id = "test";
	Exercise exercise = TestHelper.createExercise("test", "exercisename");
	exercise.setStatus(Status.ACTIVE);
	when(exerciseRepo.findOneByExerciseId(id)).thenReturn(Optional.empty());
	// when
	ExerciseCrudException actual = assertThrows(ExerciseCrudException.class,
		() -> exerciseService.deleteExercise(id));
	// then
	verify(exerciseRepo).findOneByExerciseId(id);
	verify(exerciseRepo, never()).save(exercise);
	assertThat(actual.getMessage()).isEqualTo("Exercise with id: test does not exist!");
	assertEquals(Status.ACTIVE, exercise.getStatus());
    }
}
