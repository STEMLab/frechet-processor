package goLA.exceptions;

/**
 * Created by azamat on 17. 4. 27.
 */
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
