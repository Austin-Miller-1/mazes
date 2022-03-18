package com.amw.sms.mazes;

/**
 * Exception that is thrown when client attempts to construct a maze via MazeBuilder
 * and the values set within that builder are invalid.
 */
public class InvalidMazeException extends Exception {
    private static final String EXCEPTION_MESSAGE_START =  
        "Error - Cannot build maze with current MazeBuilder state. See details:";
    
    /**
     * Constructs exception with message.
     * @param message Message indicating why the exception was thrown.
     */
    public InvalidMazeException(String message){
        super(EXCEPTION_MESSAGE_START + " " + message);
    }
}
