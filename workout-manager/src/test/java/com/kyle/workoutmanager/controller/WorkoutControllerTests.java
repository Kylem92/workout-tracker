package com.kyle.workoutmanager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kyle.workoutmanager.exceptions.WorkoutCrudException;
import com.kyle.workoutmanager.model.Workout;
import com.kyle.workoutmanager.service.WorkoutService;
import com.kyle.workoutmanager.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = WorkoutController.class)
class WorkoutControllerTests {

    @Autowired
    WorkoutController workoutController;

    @MockBean
    WorkoutService workoutService;

    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void beforeAllTests() {
	mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void beforeEachTest() {
	// prepare test
	mockMvc = MockMvcBuilders.standaloneSetup(workoutController).build();
    }

    @Test
    void contextLoads() {
	assertThat(workoutController).isNotNull();
    }

    @Test
    void testGetAllWorkouts_MVC_get() throws Exception {
	// given
	List<Workout> workouts = TestHelper.createWorkouts(3);
	when(workoutService.getAllWorkouts()).thenReturn(workouts);
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getall").accept("application/json")).andExpect(status().isOk())
		.andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<Workout> actualList = mapper.readValue(contentAsString, new TypeReference<List<Workout>>() {
	});
	verify(workoutService).getAllWorkouts();
	assertNotNull(contentAsString);
	assertEquals(3, actualList.size());
	assertEquals("1", actualList.get(0).getWorkoutId());
    }

    @Test
    void testGetAllWorkouts_MVC_get_empty() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getall").accept("application/json")).andExpect(status().isOk())
		.andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<Workout> actualList = mapper.readValue(contentAsString, new TypeReference<List<Workout>>() {
	});
	verify(workoutService).getAllWorkouts();
	assertNotNull(contentAsString);
	assertEquals(0, actualList.size());
	assertTrue(actualList.isEmpty());
    }

    @Test
    void testGetAllWorkouts_MVC_get_failure() throws Exception {
	// given
	when(workoutService.getAllWorkouts()).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getall").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(workoutService).getAllWorkouts();
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetWorkoutById_MVC_get() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	String id = "workoutId";
	when(workoutService.findById(id)).thenReturn(Optional.of(workout));
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getbyid/workoutId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	verify(workoutService).findById(id);
	assertNotNull(contentAsString);
	assertEquals(workout.getWorkoutId(), actualWorkout.getWorkoutId());
    }

    @Test
    void testGetWorkoutById_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getbyid/workoutId").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(workoutService).findById(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetWorkoutById_MVC_get_failure() throws Exception {
	// given
	String id = "workoutId";
	when(workoutService.findById(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/workout/getbyid/workoutId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(workoutService).findById(id);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testCreateWorkout_MVC_post() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.saveWorkout(workout)).thenReturn(workout);
	// when
	MvcResult actual = mockMvc.perform(post("/workout/create").content(mapper.writeValueAsString(workout))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	assertEquals(workout, actualWorkout);
	verify(workoutService).saveWorkout(workout);

    }

    @Test
    void testCreateWorkout_MVC_post_workoutname_already_exists() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.saveWorkout(workout))
		.thenThrow(new WorkoutCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(post("/workout/create").content(mapper.writeValueAsString(workout))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	assertEquals("TEST_EXCEPTION", actualWorkout.getErrorsAndWarnings().get(0));
	verify(workoutService).saveWorkout(any());
    }

    @Test
    void testCreateWorkout_MVC_post_failure() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.saveWorkout(workout)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(post("/workout/create").content(mapper.writeValueAsString(workout))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(workoutService).saveWorkout(workout);
    }

    @Test
    void testUpdateWorkout_MVC_post() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.updateWorkout(workout)).thenReturn(workout);
	// when
	MvcResult actual = mockMvc.perform(put("/workout/update").content(mapper.writeValueAsString(workout))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	assertEquals(workout, actualWorkout);
	verify(workoutService).updateWorkout(workout);

    }

    @Test
    void testUpdateWorkout_MVC_post_workoutname_already_exists() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.updateWorkout(workout))
		.thenThrow(new WorkoutCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/workout/update").content(mapper.writeValueAsString(workout))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	assertEquals("TEST_EXCEPTION", actualWorkout.getErrorsAndWarnings().get(0));
	verify(workoutService).updateWorkout(any());
    }

    @Test
    void testUpdateWorkout_MVC_post_failure() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	when(workoutService.updateWorkout(workout)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(put("/workout/update").content(mapper.writeValueAsString(workout))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(workoutService).updateWorkout(workout);
    }

    @Test
    void testDeleteWorkoutById_MVC_get() throws Exception {
	// given
	Workout workout = TestHelper.createWorkout("workoutId");
	String id = "workoutId";
	when(workoutService.deleteWorkout(id)).thenReturn(workout);
	// when
	MvcResult actual = mockMvc.perform(put("/workout/deletebyid/workoutId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	verify(workoutService).deleteWorkout(id);
	assertNotNull(contentAsString);
	assertEquals(workout.getWorkoutId(), actualWorkout.getWorkoutId());
    }

    @Test
    void testDeleteWorkoutById_MVC_get_not_found() throws Exception {
	// given
	when(workoutService.deleteWorkout(anyString()))
		.thenThrow(new WorkoutCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/workout/deletebyid/workoutId").accept("application/json"))
		.andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Workout actualWorkout = mapper.readValue(contentAsString, Workout.class);
	assertEquals("TEST_EXCEPTION", actualWorkout.getErrorsAndWarnings().get(0));
	verify(workoutService).deleteWorkout(anyString());
    }

    @Test
    void testDeleteWorkoutById_MVC_get_failure() throws Exception {
	// given
	String id = "workoutId";
	when(workoutService.deleteWorkout(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(put("/workout/deletebyid/workoutId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(workoutService).deleteWorkout(id);
	assertThat(contentAsString).isBlank();
    }

}
