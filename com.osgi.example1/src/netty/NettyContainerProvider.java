package netty;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.spi.ContainerProvider;

/**
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=jersey-netty-master/src/main/java/org/graylog2/jersey/container/netty/
 *      NettyContainerProvider.java
 */
@Provider
public class NettyContainerProvider implements ContainerProvider {

	@Override
	public <T> T createContainer(Class<T> type, Application application) throws ProcessingException {
		if (type != NettyContainer.class) {
			return null;
		}
		return type.cast(new NettyContainer(application));
	}

}
