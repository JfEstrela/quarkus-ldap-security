package br.com.jfestrela.security;

import java.security.Principal;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
public class LDAPPrincipal implements Principal{
	
	private String name;
	
	public static LDAPPrincipal of(String name) {
		return new LDAPPrincipal(name);
	}

	@Override
	public String getName() {
		return name;
	}
	
	private LDAPPrincipal(String name) {
		this.name = name;
	}

}
