package commonLib.exception;

@SuppressWarnings("serial")
public class CustomException extends Exception {
	
	public CustomException() {
	}

	public CustomException(String message) {
		super(message);
	}
}