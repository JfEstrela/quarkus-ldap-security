package br.com.jfestrela.security;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Fernando Estrela
 * @since 4 Mac 2020
 */
public class User{
	
	private String name;
	private String uid;
	private Set<String> roles;
	private String password;
	private boolean authenticated;
	
	public static User of(String name, String uid, Set<String> roles, String password,boolean authenticad) {
		return new User(name, uid, roles,password,authenticad);
	}
	
	public static User of(String uid,String password) {
		return new User(uid,password);
	}

	private User(String name, String uid, Set<String> roles, String password,boolean authenticad) {
		super();
		this.name = name;
		this.uid = uid;
		this.roles = roles == null ? new HashSet<String>() : roles;
		this.password = password;
		this.authenticated = authenticad;
	}
	
	private User(String uid,  String password) {
		super();
		this.uid = uid;
		this.password = password;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public LDAPPrincipal getPrincipal() {
		return LDAPPrincipal.of(uid);
	}

}
