package org.bee;

/**
 * A remote server error with error code and message.
 * 
 * @author dgreen
 * @version $Id: $
 */
public class BeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	long code;

	/**
	 * <p>
	 * Getter for the field <code>code</code>.
	 * </p>
	 * 
	 * @return a int.
	 */
	public long getCode() {
		return code;
	}

	/**
	 * <p>
	 * Constructor for BeeException.
	 * </p>
	 * 
	 * @param code
	 *            a int.
	 * @param message
	 *            a {@link java.lang.String} object.
	 * @param cause
	 *            a {@link java.lang.Throwable} object.
	 */
	public BeeException(long code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;

	}

	/**
	 * <p>
	 * Constructor for BeeException.
	 * </p>
	 * 
	 * @param code
	 *            a int.
	 * @param message
	 *            a {@link java.lang.String} object.
	 */
	public BeeException(long code, String message) {
		super(message);
		this.code = code;

	}

}
