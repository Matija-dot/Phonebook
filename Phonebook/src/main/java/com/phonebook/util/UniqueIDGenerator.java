package com.phonebook.util;

import java.util.List;
import java.util.Random;

public class UniqueIDGenerator {
    private static UniqueIDGenerator instance = null;
    private static Random randomGenerator;

    private UniqueIDGenerator() {
        UniqueIDGenerator.randomGenerator = new Random();
    }

    public static UniqueIDGenerator getInstance() {
        if(instance == null) {
            instance = new UniqueIDGenerator();
        }
        return instance;
    }

    public int generateNew(List<Integer> existingIDs){
        int newID;
        newID = UniqueIDGenerator.randomGenerator.nextInt();
        while (existingIDs.contains(newID))
        {
            newID = UniqueIDGenerator.randomGenerator.nextInt();
        }
        return newID;
    }
}
