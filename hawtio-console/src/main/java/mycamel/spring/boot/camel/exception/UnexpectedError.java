package mycamel.spring.boot.camel.exception;

public class UnexpectedError extends RuntimeException {

	public UnexpectedError(String message) {
		super(message);
	}

	public UnexpectedError(String message, Throwable t) {
		super(message, t);
	}

}
