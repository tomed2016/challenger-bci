/**
 * 
 */
package com.crud.challenger.exception;

/**
 * 
 */
public class EmailAlreadyExistException extends Exception {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyExistException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EmailAlreadyExistException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EmailAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}


}
