package br.com.jfestrela.security.exception;

import java.io.IOException;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
public class LDAPAuthorizationException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -512766039767460377L;

	public LDAPAuthorizationException() {
		super();
	}

	public LDAPAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public LDAPAuthorizationException(String message) {
		super(message);
	}

	public LDAPAuthorizationException(Throwable cause) {
		super(cause);
	}

}
