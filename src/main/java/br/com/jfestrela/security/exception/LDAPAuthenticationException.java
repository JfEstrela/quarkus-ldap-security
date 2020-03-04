package br.com.jfestrela.security.exception;

import java.io.IOException;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
public class LDAPAuthenticationException extends IOException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2299174973620208679L;

	public LDAPAuthenticationException() {
		super();
	}

	public LDAPAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public LDAPAuthenticationException(String message) {
		super(message);
	}

	public LDAPAuthenticationException(Throwable cause) {
		super(cause);
	}
	
}
