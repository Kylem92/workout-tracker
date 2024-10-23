package com.kyle.exercise.controller;

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
import com.kyle.exercise.exceptions.ExerciseCrudException;
import com.kyle.exercise.model.Exercise;
import com.kyle.exercise.service.ExerciseService;
import com.kyle.exercise.testhelper.TestHelper;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = ExerciseController.class)
class ExerciseControllerTests {

    @Autowired
    ExerciseController exerciseController;

    @MockBean
    ExerciseService exerciseService;

    private MockMvc mockMvc;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void beforeAllTests() {
	mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void beforeEachTest() {
	// prepare test
	mockMvc = MockMvcBuilders.standaloneSetup(exerciseController).build();
    }

    @Test
    void contextLoads() {
	assertThat(exerciseController).isNotNull();
    }

    @Test
    void testGetAllExercises_MVC_get() throws Exception {
	// given
	List<Exercise> exercises = TestHelper.createExercises(3);
	when(exerciseService.getAllExercises()).thenReturn(exercises);
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getall").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<Exercise> actualList = mapper.readValue(contentAsString, new TypeReference<List<Exercise>>() {
	});
	verify(exerciseService).getAllExercises();
	assertNotNull(contentAsString);
	assertEquals(3, actualList.size());
	assertEquals("1", actualList.get(0).getExerciseId());
    }

    @Test
    void testGetAllExercises_MVC_get_empty() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getall").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	List<Exercise> actualList = mapper.readValue(contentAsString, new TypeReference<List<Exercise>>() {
	});
	verify(exerciseService).getAllExercises();
	assertNotNull(contentAsString);
	assertEquals(0, actualList.size());
	assertTrue(actualList.isEmpty());
    }

    @Test
    void testGetAllExercises_MVC_get_failure() throws Exception {
	// given
	when(exerciseService.getAllExercises()).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getall").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).getAllExercises();
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseById_MVC_get() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "");
	String id = "exerciseId";
	when(exerciseService.findById(id)).thenReturn(Optional.of(exercise));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyid/exerciseId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	verify(exerciseService).findById(id);
	assertNotNull(contentAsString);
	assertEquals(exercise.getExerciseId(), actualExercise.getExerciseId());
    }

    @Test
    void testGetExerciseById_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyid/exerciseId").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findById(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseById_MVC_get_failure() throws Exception {
	// given
	String id = "exerciseId";
	when(exerciseService.findById(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyid/exerciseId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findById(id);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseByExercisename_MVC_get() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	String exerciseName = "exercisename";
	when(exerciseService.findByName(exerciseName)).thenReturn(Optional.of(exercise));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyname/exercisename").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	verify(exerciseService).findByName(exerciseName);
	assertNotNull(contentAsString);
	assertEquals(exercise.getExerciseId(), actualExercise.getExerciseId());
	assertEquals(exercise.getName(), actualExercise.getName());
    }

    @Test
    void testGetExerciseByExercisename_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyname/exercisename").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findByName(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseByExercisename_MVC_get_failure() throws Exception {
	// given

	String exerciseName = "exercisename";
	when(exerciseService.findByName(exerciseName)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/getbyname/exercisename").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findByName(exerciseName);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseByType_MVC_get() throws Exception {
	// given
	String type = "type";
	Exercise exercise = TestHelper.createExercise("exerciseId", "type");
	when(exerciseService.findByType(type)).thenReturn(Optional.of(exercise));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/findbytype/type").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	verify(exerciseService).findByType(type);
	assertNotNull(contentAsString);
	assertEquals(exercise.getExerciseId(), actualExercise.getExerciseId());
	assertEquals(exercise.getName(), actualExercise.getName());
	assertEquals(exercise.getType(), actualExercise.getType());
    }

    @Test
    void testGetExerciseByType_MVC_get_not_found() throws Exception {
	// given
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/findbytype/exercisename").accept("application/json"))
		.andExpect(status().isNotFound()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findByType(anyString());
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testGetExerciseByType_MVC_get_failure() throws Exception {
	// given
	String type = "type";
	when(exerciseService.findByType(type)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(get("/exercise/findbytype/type").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).findByType(type);
	assertThat(contentAsString).isBlank();
    }

    @Test
    void testCreateExercise_MVC_post() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.saveExercise(exercise)).thenReturn(exercise);
	// when
	MvcResult actual = mockMvc.perform(post("/exercise/create").content(mapper.writeValueAsString(exercise))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	assertEquals(exercise, actualExercise);
	verify(exerciseService).saveExercise(exercise);

    }

    @Test
    void testCreateExercise_MVC_post_exercisename_already_exists() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.saveExercise(exercise))
		.thenThrow(new ExerciseCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(post("/exercise/create").content(mapper.writeValueAsString(exercise))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	assertEquals("TEST_EXCEPTION", actualExercise.getErrorsAndWarnings().get(0));
	verify(exerciseService).saveExercise(any());
    }

    @Test
    void testCreateExercise_MVC_post_failure() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.saveExercise(exercise)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(post("/exercise/create").content(mapper.writeValueAsString(exercise))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(exerciseService).saveExercise(exercise);
    }

    @Test
    void testUpdateExercise_MVC_post() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.updateExercise(exercise)).thenReturn(exercise);
	// when
	MvcResult actual = mockMvc.perform(put("/exercise/update").content(mapper.writeValueAsString(exercise))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	assertEquals(exercise, actualExercise);
	verify(exerciseService).updateExercise(exercise);

    }

    @Test
    void testUpdateExercise_MVC_post_exercisename_already_exists() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.updateExercise(exercise))
		.thenThrow(new ExerciseCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/exercise/update").content(mapper.writeValueAsString(exercise))
		.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	assertEquals("TEST_EXCEPTION", actualExercise.getErrorsAndWarnings().get(0));
	verify(exerciseService).updateExercise(any());
    }

    @Test
    void testUpdateExercise_MVC_post_failure() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "exercisename");
	when(exerciseService.updateExercise(exercise)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc
		.perform(put("/exercise/update").content(mapper.writeValueAsString(exercise))
			.contentType(MediaType.APPLICATION_JSON_VALUE))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	assertThat(contentAsString).isBlank();
	verify(exerciseService).updateExercise(exercise);
    }

    @Test
    void testDeleteExerciseById_MVC_get() throws Exception {
	// given
	Exercise exercise = TestHelper.createExercise("exerciseId", "");
	String id = "exerciseId";
	when(exerciseService.deleteExercise(id)).thenReturn(exercise);
	// when
	MvcResult actual = mockMvc.perform(put("/exercise/deletebyid/exerciseId").accept("application/json"))
		.andExpect(status().isOk()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	verify(exerciseService).deleteExercise(id);
	assertNotNull(contentAsString);
	assertEquals(exercise.getExerciseId(), actualExercise.getExerciseId());
    }

    @Test
    void testDeleteExerciseById_MVC_get_not_found() throws Exception {
	// given
	when(exerciseService.deleteExercise(anyString()))
		.thenThrow(new ExerciseCrudException("TEST_EXCEPTION", HttpStatus.FORBIDDEN));
	// when
	MvcResult actual = mockMvc.perform(put("/exercise/deletebyid/exerciseId").accept("application/json"))
		.andExpect(status().isForbidden()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	Exercise actualExercise = mapper.readValue(contentAsString, Exercise.class);
	assertEquals("TEST_EXCEPTION", actualExercise.getErrorsAndWarnings().get(0));
	verify(exerciseService).deleteExercise(anyString());
    }

    @Test
    void testDeleteExerciseById_MVC_get_failure() throws Exception {
	// given
	String id = "exerciseId";
	when(exerciseService.deleteExercise(id)).thenThrow(new RuntimeException("TEST_EXCEPTION"));
	// when
	MvcResult actual = mockMvc.perform(put("/exercise/deletebyid/exerciseId").accept("application/json"))
		.andExpect(status().isInternalServerError()).andReturn();
	// then
	String contentAsString = actual.getResponse().getContentAsString();
	verify(exerciseService).deleteExercise(id);
	assertThat(contentAsString).isBlank();
    }

}
