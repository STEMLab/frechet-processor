package io.github.stemlab.utils;

import io.github.stemlab.exceptions.CustomException;

import java.io.File;

/**
 * Created by Azamat on 7/24/2017.
 */
public class Validator {
    public static void checkArguments(String... args) {
        if (args.length != 3) new CustomException("Number of arguments are wrong");
        else if (!new File(args[0]).isFile()) new CustomException("File " + args[0] + " not found");
        else if (!new File(args[1]).isFile()) new CustomException("File " + args[1] + " not found");

        File output = new File(args[2]);
        if (!output.exists() && !output.isDirectory()) {
            new CustomException("Output path is not directory or doesn't exist");
        }
    }
}
