package com.amw.sms.algorithms.generation;

import com.amw.sms.AppConfiguration;
import com.amw.sms.util.Utilities;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for WilsonsAlgorithm.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {WilsonsAlgorithm.class, Utilities.class, AppConfiguration.class})
public class WilsonsAlgorithmTest extends MazeGenAlgorithmTest {
    @Autowired
    private WilsonsAlgorithm wilsonsAlgorithm;
    
    /**
     * {@inheritDoc}
     */
    @Override
    MazeGenAlgorithm getGenAlgorithmUnderTest() {
        return wilsonsAlgorithm;
    }
}
