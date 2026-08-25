package bob.exception;

/**
 * Represents an exception thrown to trigger application exit.
 */
public class ExitException extends Exception {

    /**
     * Constructs an ExitException with the specified message.
     *
     * @param message the exit message
     */
    public ExitException(String message) {
        super(message);
    }
}
