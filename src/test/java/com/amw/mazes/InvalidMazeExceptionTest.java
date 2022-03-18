package com.amw.mazes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class InvalidMazeExceptionTest {
    @Test
    public void testInvalidMazeException(){
        final var exampleMessage = "Invalid size";
        final var exception = new InvalidMazeException(exampleMessage);
        assertTrue(exception.getMessage().contains(exampleMessage));
    }    
}
