package br.com.jfestrela.security;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import br.com.jfestrela.security.exception.LDAPAuthenticationException;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.UsernamePasswordAuthenticationRequest;
/**
 * Provaider de autenticacao no LDAP.
 * 
 * @author Fernando Estrela
 * @since 2 Mac 2020
 */
@ApplicationScoped
public class IndentityLdapProvaider implements IdentityProvider<UsernamePasswordAuthenticationRequest> {

	@Inject
	private LDAPSecurityHandler handlerAuthentication;
	
    @Override
    public Class<UsernamePasswordAuthenticationRequest> getRequestType() {
        return UsernamePasswordAuthenticationRequest.class;
    }

    @Override
    public CompletionStage<SecurityIdentity> authenticate(UsernamePasswordAuthenticationRequest request,
            AuthenticationRequestContext context) {
        return context.runBlocking(new Supplier<SecurityIdentity>() {
            @Override
            public SecurityIdentity get() {
                try {
                    SecurityLdapIdentity.Builder builder = SecurityLdapIdentity.builder();
                    User userRequest = User.of(request.getUsername(), new String(request.getPassword().getPassword()));
                    User userLdap = handlerAuthentication.authentication(userRequest);
                    builder.setPrincipal(userLdap.getPrincipal());
                    builder.addRoles(userLdap.getRoles());                 
                    builder.addCredential(request.getPassword());
                    return builder.build();
                } catch (SecurityException | LDAPAuthenticationException e) {
                    throw new AuthenticationFailedException();
                }
            }
        });
    }
}
