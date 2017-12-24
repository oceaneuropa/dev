package netty;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.sun.security.auth.UserPrincipal;

/**
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=jersey-netty-master/src/main/java/org/graylog2/jersey/container/netty/
 *      DefaultSecurityContextFactory.java
 */
public class DefaultSecurityContextFactory implements SecurityContextFactory {

	@Override
	public SecurityContext create(final String userName, String credential, final boolean isSecure, final String authcScheme, String host) {
		final String principal = userName;
		return new SecurityContext() {
			@Override
			public Principal getUserPrincipal() {
				return new UserPrincipal(principal);
			}

			@Override
			public boolean isUserInRole(String role) {
				return false;
			}

			@Override
			public boolean isSecure() {
				return isSecure;
			}

			@Override
			public String getAuthenticationScheme() {
				return authcScheme;
			}
		};
	}

}
