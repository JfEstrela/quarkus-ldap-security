package br.com.jfestrela.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.util.StringUtils;

import br.com.jfestrela.endpoint.response.ApiResponse;
import br.com.jfestrela.security.exception.LDAPAuthenticationException;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
@Provider
public class LdapSecurityExceptionHandler implements ExceptionMapper<Throwable>{
	
	public static final int REGISTRO_SITUACAO_STATUS_ERRO = 5;

	@Override
	public Response toResponse(Throwable exception) {	
		if(exception instanceof LDAPAuthenticationException ) {
			return processaResponse(exception,401);
		}
		return processaResponse(exception,200);
	}

	private Response processaResponse(Throwable exception,int code) {
		return Response.status(code).entity(ApiResponse.of(code,
				getMessageException(exception))).build();
	}

	private boolean hasMessageCauseException(Throwable exception){
		return exception.getCause() != null && exception.getCause().getMessage() != null;
	}
	
	private boolean hasMessageException(Throwable exception){
		return exception.getMessage() != null;
	}
	
	private String getMessageException(Throwable exception) {
		return hasMessageException(exception) ? getMessageExceptionAndCauseMessage(exception) : getMessageCauseException(exception);
	}
	
	private String getMessageExceptionAndCauseMessage(Throwable exception) {
		return exception.getMessage().concat(getCausa(exception));
	}

	private String getCausa(Throwable exception) {
		String causa = getMessageCauseException(exception);
		if(StringUtils.isEmpty(causa)) {
			return causa;
		}
		return " (Causa: "+getMessageCauseException(exception)+")";
	}
	
	private String getMessageCauseException(Throwable exception) {
		return hasMessageCauseException(exception) ? exception.getCause().getMessage() : "";
	}

}
