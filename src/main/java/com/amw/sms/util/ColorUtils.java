package com.amw.sms.util;

import org.springframework.stereotype.Component;

import java.awt.Color;

@Component
public class ColorUtils {
    private static final int LIGHTEN_VALUE = 100;

    public ColorUtils(){}

    private int getLighterValue(int colorValue){
        final var newValue = colorValue + LIGHTEN_VALUE;
        return newValue >= 255
            ? 255
            : newValue;
    }

    public Color getLighterColor(Color color){
        return new Color(
            this.getLighterValue(color.getRed()),
            this.getLighterValue(color.getGreen()),
            this.getLighterValue(color.getBlue())
        );
    }
}
