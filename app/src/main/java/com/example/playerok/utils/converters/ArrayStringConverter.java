package com.example.playerok.utils.converters;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayStringConverter {

    public static String arrayToString (ArrayList<String> pathArray) {
        StringBuilder builder = new StringBuilder();
        builder.append(pathArray.get(0));
        for (int i = 1; i < pathArray.size(); i++) {
            builder.append("AND").append(pathArray.get(i));
        }
        return builder.toString();
    }

    public static ArrayList<String> stringToArray (String stringPathArray){
        return new ArrayList<>(Arrays.asList(stringPathArray.split("AND")));
    }
}
