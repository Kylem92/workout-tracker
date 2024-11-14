package com.kyle.workoutmanager.producer;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.kyle.workoutmanager.configs.KafkaTopicConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg) {
	CompletableFuture<SendResult<String, String>> future = kafkaTemplate
		.send(KafkaTopicConfig.WORKOUT_MANAGER_CSV_TOPIC, msg);
	future.whenComplete((result, ex) -> {
	    if (ex == null) {
		log.info("Sent message=[" + msg + "] with offset=[" + result.getRecordMetadata().offset() + "]");
	    } else {
		log.info("Unable to send message=[" + msg + "] due to : " + ex.getMessage());
	    }
	});
    }
}
