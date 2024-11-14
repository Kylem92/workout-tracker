package com.kyle.workoutmanager.configs;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = KafkaTopicConfig.class)
class KafkaTopicConfigTests {

    @Autowired
    KafkaTopicConfig underTest;

    @Test
    void testProducerFactory() {
	// given
	// when
	ProducerFactory<String, String> actual = underTest.producerFactory();
	// then
	assertThat(actual).isNotNull();
    }

    @Test
    void testKafkaTemplate() throws Exception {
	// given
	// when
	KafkaTemplate<String, String> actual = underTest.kafkaTemplate();
	// then
	assertThat(actual).isNotNull();
	assertThat(actual.getProducerFactory()).isNotNull();
    }

    @Test
    void testNewTopic() {
	// given
	// when
	NewTopic actual = underTest.topic1();
	// then
	assertThat(actual).isNotNull();
	assertThat(actual.name()).isEqualTo("workoutmanagercsv");
	assertThat(actual.numPartitions()).isEqualTo(1);
	assertThat(actual.replicationFactor()).isEqualTo((short) 1);
    }
}
