package br.com.jfestrela.service.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
@Liveness
@ApplicationScoped
public class LdapSecurityHealthCheckService implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("Seu serviço está online").up().build();
    }

}
