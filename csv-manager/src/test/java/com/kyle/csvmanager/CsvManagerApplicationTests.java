package com.kyle.csvmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = CsvManagerApplication.class)
class CsvManagerApplicationTests {

    @Test
    void contextLoads() {
    }

}
