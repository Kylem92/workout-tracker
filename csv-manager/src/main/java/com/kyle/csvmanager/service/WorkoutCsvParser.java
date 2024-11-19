package com.kyle.csvmanager.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.kyle.csvmanager.model.Exercise;
import com.kyle.csvmanager.model.Workout;

@Service
public class WorkoutCsvParser {

    public List<Workout> parseWorkoutFile(MultipartFile file) throws IOException {
	List<Workout> workoutDays = new ArrayList<>();

	try (ICsvBeanReader beanReader = new CsvBeanReader(new InputStreamReader(file.getInputStream()),
		CsvPreference.STANDARD_PREFERENCE)) {

	    int currentWeek = 0;
	    int currentDay = 0;
	    Workout currentWorkoutDay = null;

	    String[] line;
	    while ((line = beanReader.getHeader(false)) != null) {
		if (line[0] != null && line[0].startsWith("Week")) {
		    currentWeek = Integer.parseInt(line[0].split(" ")[1]);
		} else if (line[0] != null && line[0].startsWith("Day")) {
		    currentDay = Integer.parseInt(line[0].split(" ")[1]);
		    currentWorkoutDay = new Workout(currentWeek, currentDay);
		    workoutDays.add(currentWorkoutDay);
		    beanReader.getHeader(false);
		} else if (line[0] != null && !line[0].isEmpty() && currentWorkoutDay != null) {
		    Exercise exercise = new Exercise();
		    exercise.setName(line[0]);
		    exercise.setSets(parseIntOrDefault(line[1], 0));
		    exercise.setReps(parseIntOrDefault(line[2], 0));
		    exercise.setIntensity(line[3]);
		    exercise.setLoad(parseDoubleOrDefault(line[4], 0.0));
		    exercise.setTempo(line[5]);
		    exercise.setRest(parseIntOrDefault(line[6], 0));

		    currentWorkoutDay.addExercise(exercise);
		}
	    }
	}

	return workoutDays;
    }

    private int parseIntOrDefault(String value, int defaultValue) {
	try {
	    return value == null || value.trim().isEmpty() ? defaultValue : Integer.parseInt(value.trim());
	} catch (NumberFormatException e) {
	    return defaultValue;
	}
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
	try {
	    return value == null || value.trim().isEmpty() ? defaultValue : Double.parseDouble(value.trim());
	} catch (NumberFormatException e) {
	    return defaultValue;
	}
    }

}
