package goLA.exceptions;

/**
 * Created by azamat on 17. 4. 27.
 */
public class CustomException {
    public CustomException(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
