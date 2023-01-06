package uk.co.polycode.mdcms.util.jms;

/**
 * An exception to indicate a message was not processed because it cannot be processed in it's current state
 */
public class InvalidMessageException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidMessageException() {
		super();
	}

	public InvalidMessageException(final String message) {
		super(message);
	}

	public InvalidMessageException(final Throwable cause) {
		super(cause);
	}

	public InvalidMessageException(final String message, final Throwable cause) {
		this(message, cause, true, true);
	}

	protected InvalidMessageException(final String message, final Throwable cause,
	                                  final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
