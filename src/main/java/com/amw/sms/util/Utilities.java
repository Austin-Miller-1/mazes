package com.amw.sms.util;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Contains assortment of utility methods.
 */
@Component
public class Utilities {
    @Autowired
    private Random random;
    
    /**
     * Constructs new collection-utils instance.
     */
    public Utilities(){}

    /**
     * Returns a random element from the provided list. List must not be empty.
     * @param <T> Type of the list's elements.
     * @param list The list.
     * @return A random element from the list.
     * @throws IllegalArgumentException if empty list is provided.
     */
    public <T> T randomElementFrom(List<T> list){
        if(list.isEmpty()){
            throw new IllegalArgumentException("method does not accept empty lists");
        }

        return list.get(random.nextInt(list.size()));
    }
}
