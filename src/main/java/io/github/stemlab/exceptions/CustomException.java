package io.github.stemlab.exceptions;


public class CustomException {
    /**
     * Display message in console and stop execution
     *
     * @param message
     */
    public CustomException(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
