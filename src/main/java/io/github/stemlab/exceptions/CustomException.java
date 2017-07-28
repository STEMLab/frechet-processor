package io.github.stemlab.exceptions;


public class CustomException {
    public CustomException(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
