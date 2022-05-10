package com.amw.sms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for AppConfiguration.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AppConfiguration.class})
public class AppConfigurationTest {
    @Autowired
    private AppConfiguration appConfiguration;

    @Test
    void testGetRandom_returnsSameInstanceEachTime() {
        assertNotNull(appConfiguration.getRandom());
        assertEquals(appConfiguration.getRandom(), appConfiguration.getRandom());
    }
}
