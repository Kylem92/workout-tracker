package com.kyle.csvmanager.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.kyle.csvmanager.model.Exercise;
import com.kyle.csvmanager.model.Workout;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkoutCsvWriter {

    public void writeWorkoutData(List<Workout> workoutDays) throws IOException {
	try (CsvListWriter csvWriter = new CsvListWriter(new FileWriter("workout-program.csv"),
		CsvPreference.STANDARD_PREFERENCE)) {

	    workoutDays.sort(Comparator.comparing(Workout::getWeekNumber).thenComparing(Workout::getDayNumber));

	    int weekNumber = 0;
	    for (Workout workoutDay : workoutDays) {
		if (workoutDay.getWeekNumber() > weekNumber) {
		    weekNumber = workoutDay.getWeekNumber();
		    csvWriter.write(Arrays.asList("Week " + weekNumber));
		    csvWriter.writeHeader(""); // Empty line
		}
		writeExerciseData(csvWriter, workoutDay);
	    }
	} catch (IOException e) {
	    log.error("CSV write failure: {} - {}", workoutDays.get(0).getUserId(), e.getMessage(), e);
	    throw e;
	}
    }

    private static void writeExerciseData(CsvListWriter csvWriter, Workout workoutDay) throws IOException {
	csvWriter.write(Arrays.asList("Day " + workoutDay.getDayNumber()));
	csvWriter.writeHeader("");
	csvWriter.write(Arrays.asList("Exercise", "Sets", "Reps", "Intensity", "Load", "Tempo", "Rest"));
	List<Exercise> exercises = workoutDay.getExercises();
	for (Exercise exercise : exercises) {
	    csvWriter.write(exercise.toCsvList());
	}

	csvWriter.writeHeader("");
    }

}
