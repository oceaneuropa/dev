package netty;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;

/**
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=jersey-netty-master/src/main/java/org/graylog2/jersey/container/netty/
 *      HeaderAwareSecurityContext.java
 */
public interface HeaderAwareSecurityContext extends SecurityContext {

	void setHeaders(MultivaluedMap<String, String> headers);

}
