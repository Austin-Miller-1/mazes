package com.amw.sms.util;

import java.util.Random;

public class CoinFlip {
    private final int headsLikelihood;
    private final Random rng;

    public CoinFlip(){
        this(50);
    }

    public CoinFlip(int headsLikelihood){
        if(headsLikelihood < 0) headsLikelihood = 0;
        else if(headsLikelihood > 100) headsLikelihood = 100;
        this.headsLikelihood = headsLikelihood;

        this.rng = new Random();
    }

    public boolean isHeads(){
        return rng.nextInt(100) < headsLikelihood;
    }

    public boolean isTails(){
        return !this.isHeads();
    }
}
