package com.junit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootTestingApplicationTests {
    SpringBootTestingApplication springBootTestingApplication;

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

    @Test
    void testComponentProcessingMicroserviceApplication() {
        org.assertj.core.api.Assertions.assertThat(springBootTestingApplication).isNull();
    }

}
