package com.kyle.csvmanager.consumer;

import java.io.IOException;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.kyle.commonutils.JsonUtils;
import com.kyle.csvmanager.model.Workout;
import com.kyle.csvmanager.service.WorkoutCsvWriter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaConsumer {

    private final WorkoutCsvWriter workoutCsvWriter;

    @KafkaListener(topics = "workoutmanagercsv", groupId = "csv_manager")
    public void listenWorkoutManager(String message) {
	log.info("Received Message in group csv_manager: " + message);
	try {
	    workoutCsvWriter.writeWorkoutData(JsonUtils.objectListFromJSON(message, Workout.class));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
