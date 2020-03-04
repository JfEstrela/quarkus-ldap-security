package br.com.jfestrela.security;

import javax.inject.Inject;
import javax.inject.Singleton;

import br.com.jfestrela.security.exception.LDAPAuthenticationException;
import br.com.jfestrela.security.property.LDAPProperties;

/**
 * Handler de autenticacao no LDAP.
 * 
 * @author Fernando Estrela
 * @since 2 Mac 2020
 */
@Singleton
public class LDAPSecurityHandler {
	
	@Inject
	private LDAPProperties properties;

	private LDAPAuthentication ldapAuthentication;
	
	public User authentication(User user)
			throws LDAPAuthenticationException {
		User userAuth = null;
		try {
			this.ldapAuthentication = LDAPAuthentication.of(properties);
			if(null == user) {
				throw new LDAPAuthenticationException("401 Unauthorized. Usuario ou Senha invalidos.");
			}
			userAuth = ldapAuthentication.authentication(user);
			
		}catch (Exception e) {
			throw new LDAPAuthenticationException("401 Unauthorized. ",e);
		}
		return userAuth;
	}

}
