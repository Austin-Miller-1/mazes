package com.amw.sms.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import com.amw.sms.AppConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests for utility methods.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Utilities.class, AppConfiguration.class})
public class UtilitiesTest {    
    @Autowired
    private Utilities utilities;

    @Test
    void testRandomElementFrom_whenProvidedEmptyList_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            utilities.randomElementFrom(new ArrayList<Integer>());
        });
    }

    @Test
    void testRandomElementFrom_whenProvidedListOfOneElement_returnsThatElement() {
        final var sampleValue = Integer.valueOf(1);
        final var list = Arrays.asList(sampleValue);
        assertEquals(sampleValue, utilities.randomElementFrom(list));        
    }

    @Test
    void testRandomElementFrom_whenProvidedListOfelements_returnsRandomElementFromList() {
       final var list = Arrays.asList(1, 2, 3);
       assertTrue(list.contains(utilities.randomElementFrom(list)));
    }
}
