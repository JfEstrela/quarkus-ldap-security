package br.com.jfestrela.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.jfestrela.security.property.LDAPProperties;

/**
 * Classe responsavel em acessar o LDAP.
 * 
 * @author Fernando Estrela
 * @since 2 Mac 2020
 */
public class LDAPAuthentication {

	private static final String LDAP_USE_SSL = "LDAP_USE_SSL";
	private static final String ATTRIBUTES_BINARY = "java.naming.ldap.attributes.binary";

	private static final Logger LOG = LoggerFactory.getLogger(LDAPAuthentication.class);

	private LDAPProperties properties;

	private LDAPAuthentication(LDAPProperties properties) {
		this.properties = properties;
	}

	public static LDAPAuthentication of(LDAPProperties properties) {
		return new LDAPAuthentication(properties);
	}

	public User authentication(User user) throws NamingException {
		LdapContext connection = null;
		LdapContext connectionAuth = null;
		try {
			connection = connect(initEnv(this.properties.getUsername(), this.properties.getPassword()));
			SearchResult serResult = this.findAccountByAccountName(connection, 
					                                               this.properties.getSearchBase(),
					                                               user.getUid()
					                                               );
			String dnUserAuth = serResult.getName().concat(",".concat(this.properties.getSearchBase()));
			Set<String> roles = getRoleNamesForUser(user.getUid(), connection, dnUserAuth);
			connectionAuth = this.connect(this.initEnv(dnUserAuth, user.getPassword()));
			String nameUser = (String) serResult.getAttributes().get(this.properties.getDisplayName()).get();
			return User.of(nameUser, user.getUid(), roles, null, true);
		} finally {
			closseLdapConnections(connection, connectionAuth);
		}
	}

	private void closseLdapConnections(LdapContext... connections ) throws NamingException {
		Arrays.asList(connections).stream()
								  .filter(conn -> null != conn)
								  .forEach(conn -> 
								  		   this.closeConnection(conn)
										   );
	}

	private void closeConnection(LdapContext conn) {
		try {
			conn.close();
		} catch (NamingException e) {
			LOG.error("Erro ao fechar conexao com LDAP");
		}
	}

	public boolean authorized(User user, String grupo) throws NamingException {
		LdapContext connection = connect(initEnv(this.properties.getUsername(), this.properties.getPassword()));
		try {
			NamingEnumeration<SearchResult> results = this.findAccountByGrupo(connection,
					this.properties.getSearchBase(), grupo);
			
			while (results.hasMoreElements()) {
				SearchResult searchResult = results.nextElement();
				String uid = (String) searchResult.getAttributes().get("uid").get();
				if (uid.equals(user.getUid())) {
					return true;
				}
			}
			return false;
		} finally {
			connection.close();
		}
	}

	private InitialLdapContext connect(Hashtable<String, Object> env) throws NamingException {
		return new InitialLdapContext(env, null);
	}

	public SearchResult findAccountByAccountName(DirContext ctx, String ldapSearchBase, String accountName)
			throws NamingException {
		String searchFilter = "(uid=".concat(accountName).concat(")");
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);

		SearchResult searchResult = null;
		if (results.hasMoreElements()) {
			searchResult = (SearchResult) results.nextElement();
			if (results.hasMoreElements()) {
				LOG.error("Multiplos usuarios para o accountName: " + accountName);
				return null;
			}
		}
		return searchResult;
	}

	public NamingEnumeration<SearchResult> findAccountByGrupo(DirContext ctx, String ldapSearchBase, String grupo)
			throws NamingException {
		String searchFilter = "(&(memberOf=cn=".concat(grupo).concat(",ou=grupos,ou=datacenter,dc=teste,dc=local))");
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);
		return results;
	}

	public Set<String> getRoleNamesForUser(String username, LdapContext ldapContext, String userDnTemplate)
			throws NamingException {
		try {
			Set<String> roleNames = new LinkedHashSet<>();

			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String searchFilter = "(&(objectClass=groupOfNames)(member=".concat(userDnTemplate).concat("))");
			Object[] searchArguments = new Object[] { username };

			NamingEnumeration<?> answer = ldapContext.search(
					properties.getSearchBase(), searchFilter, searchArguments,
					searchCtls);

			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					this.extracteRoles(roleNames, attrs);
				}
			}
			return roleNames;

		} catch (Exception e) {
			LOG.error("Error ao carregar roles.", e);
			return new HashSet<>();
		}


	}

	private void extracteRoles(Set<String> roleNames, Attributes attrs) throws NamingException {
			NamingEnumeration<?> ae = attrs.getAll();
		while (ae.hasMore()) {
			Attribute attr = (Attribute) ae.next();
			if (attr.getID().equals("cn")) {
				roleNames.add((String) attr.get());
			}
		}
	}

	private Hashtable<String, Object> initEnv(String user, String password) {
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		env.put(Context.SECURITY_AUTHENTICATION, this.properties.getSecurityAuthentication());
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, this.properties.getContextFactory());
		env.put(Context.PROVIDER_URL, this.properties.getServer());
		env.put(LDAP_USE_SSL, this.properties.isUseSSL());
		env.put(ATTRIBUTES_BINARY, this.properties.getAttributesBinary());
		return env;
	}
}
