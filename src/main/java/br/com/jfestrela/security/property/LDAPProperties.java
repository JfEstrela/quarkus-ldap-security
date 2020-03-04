package br.com.jfestrela.security.property;

import io.quarkus.arc.config.ConfigProperties;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
@ConfigProperties(prefix = "ldap")
public class LDAPProperties {
	
	private String server;
    private String searchBase;
    private String username;
    private String password;
    private String attributesBinary;
    private String displayName;
    private String securityAuthentication;
    private String contextFactory;
    private boolean useSSL;  
    
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getSearchBase() {
		return searchBase;
	}
	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isUseSSL() {
		return useSSL;
	}
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
	public String getAttributesBinary() {
		return attributesBinary;
	}
	public void setAttributesBinary(String attributesBinary) {
		this.attributesBinary = attributesBinary;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getSecurityAuthentication() {
		return securityAuthentication;
	}
	public void setSecurityAuthentication(String securityAuthentication) {
		this.securityAuthentication = securityAuthentication;
	}
	public String getContextFactory() {
		return contextFactory;
	}
	public void setContextFactory(String contextFactory) {
		this.contextFactory = contextFactory;
	}
}
