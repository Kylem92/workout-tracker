package com.kyle.workoutmanager.producer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = KafkaProducer.class)
class KafkaProducerTests {

    @Autowired
    KafkaProducer underTest;

    @MockBean
    KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private CompletableFuture<SendResult<String, String>> future;

    @Test
    void testSendMessage() {
	// given
	when(kafkaTemplate.send(anyString(), anyString())).thenReturn(CompletableFuture.completedFuture(
		new SendResult<>(new ProducerRecord<>("test", "test"), new RecordMetadata(null, 0, 0, 0, 0, 0))));
	// when
	underTest.sendMessage("test");
	// then
	verify(kafkaTemplate).send(anyString(), anyString());
    }

    @Test
    void testSendMessage_failure() {
	// given
	when(kafkaTemplate.send(anyString(), anyString()))
		.thenReturn(CompletableFuture.failedFuture(new RuntimeException()));
	// when
	underTest.sendMessage("test");
	// then
	verify(kafkaTemplate).send(anyString(), anyString());
    }

}
