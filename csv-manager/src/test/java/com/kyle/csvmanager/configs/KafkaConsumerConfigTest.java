package com.kyle.csvmanager.configs;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = KafkaConsumerConfig.class)
class KafkaConsumerConfigTest {

    @Autowired
    KafkaConsumerConfig underTest;

    @Test
    void testKafkaListenerContainerDataUpdateEventFactory() {
	// given
	// when
	ConcurrentKafkaListenerContainerFactory<String, String> actual = underTest.kafkaListenerContainerFactory();
	// then
	assertThat(actual).isNotNull();
    }

}
