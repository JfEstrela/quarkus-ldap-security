package br.com.jfestrela.service;

import java.security.Principal;

import javax.enterprise.context.ApplicationScoped;

import br.com.jfestrela.endpoint.response.ApiResponse;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
@ApplicationScoped
public class LdapSecurityService {

    public ApiResponse helloWorld(Principal principal) {
    	 String name = principal != null ? principal.getName() : "anonymous";
        return ApiResponse.of(200, "Bem vindo ao Quarkus security com LDAP ".concat(name).concat(" !"));
    }

}