package netty;

import javax.ws.rs.core.SecurityContext;

/**
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=jersey-netty-master/src/main/java/org/graylog2/jersey/container/netty/
 *      SecurityContextFactory.java
 * 
 */
public interface SecurityContextFactory {

	SecurityContext create(String userName, String credential, boolean isSecure, String authcScheme, String host);

}