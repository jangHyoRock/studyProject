package commonLib.exception;

/*
 * Handling exceptions that occur when parameters are invalid.
 */
@SuppressWarnings("serial")
public class InvalidParameterException extends Exception {
	
	public InvalidParameterException() {
	}

	public InvalidParameterException(String message) {
		super(message);
	}
}
