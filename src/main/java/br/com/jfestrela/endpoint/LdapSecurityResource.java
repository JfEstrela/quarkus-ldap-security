package br.com.jfestrela.endpoint;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;

import java.security.Principal;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import br.com.jfestrela.service.LdapSecurityService;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
@Tag(name = "quarkus-ldap-security")
@Path("/quarkus-ldap")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class LdapSecurityResource {

	@Inject
	private LdapSecurityService service;

    @GET
    @Path("/hello")
    @RolesAllowed(value = {"topdesk"})
    public CompletionStage<Response> value(@Context SecurityContext sec) throws InterruptedException, ExecutionException {
    	Principal principal = sec.getUserPrincipal(); 
        return supplyAsync(ok().entity(service.helloWorld(principal))::build);
    }
}