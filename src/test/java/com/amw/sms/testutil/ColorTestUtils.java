package com.amw.sms.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import ij.ImagePlus;

/**
 * Helper methods for color-based tests.
 */
public class ColorTestUtils {
    /**
     * Asserts that pixel in the image at the specified coordinates is of the expected color.
     * @param expectedColor Color that the pixel is expected of being.
     * @param image Image to check.
     * @param x x-coordinate of the pixel.
     * @param y y-coordinate of the pixel.
     */
    public void assertColorAtPixel(Color expectedColor, ImagePlus image, int x, int y){
        final var actualCellPixel = image.getPixel(x, y);
        assertEquals(expectedColor.getRed(), actualCellPixel[0]);     //Red
        assertEquals(expectedColor.getGreen(), actualCellPixel[1]);   //Green
        assertEquals(expectedColor.getBlue(), actualCellPixel[2]);    //Blue
    }

    private boolean isDarker(int expectedDarker, int expectedLighter){
        return expectedDarker == 0 || expectedDarker < expectedLighter;
    }

    private boolean isLighter(int expectedLighter, int expectedDarker){
        return expectedLighter == 255 || expectedDarker < expectedLighter;
    }

    /**
     * Asserts that the first color is darker than the second color. In this method, "darker" is
     * defined as being composed of lower red, green AND blue values. If any of the three 
     * are not lower, the assertion fails. 
     * @param expectedDarker
     * @param expectedBrighter
     */
    public void assertDarkerColor(Color expectedDarker, Color expectedBrighter){
        assertTrue(isDarker(expectedDarker.getRed(), expectedBrighter.getRed()));
        assertTrue(isDarker(expectedDarker.getGreen(), expectedBrighter.getGreen()));
        assertTrue(isDarker(expectedDarker.getBlue(), expectedBrighter.getBlue()));
    }

    /**
     * Asserts that the first color is brighter than the second color. In this method, "brighter" is
     * defined as being composed of higher red, green AND blue values. If any of the three 
     * are not higher, the assertion fails. 
     * @param expectedDarker
     * @param expectedDarker
     */
    public void assertLighterColor(Color expectedBrighter, Color expectedDarker){
        assertTrue(isLighter(expectedBrighter.getRed(), expectedDarker.getRed()));
        assertTrue(isLighter(expectedBrighter.getGreen(), expectedDarker.getGreen()));
        assertTrue(isLighter(expectedBrighter.getBlue(), expectedDarker.getBlue()));
    }
}
