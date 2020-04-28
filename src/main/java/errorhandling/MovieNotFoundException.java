package errorhandling;

/**
 *
 * @author allan
 */
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException(String message) {
        super(message);
    }

    public MovieNotFoundException() {
        super("Requested item could not be found");
    }
}
