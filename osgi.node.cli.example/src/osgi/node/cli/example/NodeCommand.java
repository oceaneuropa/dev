package osgi.node.cli.example;

import java.util.Hashtable;

import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import osgi.node.client.example.NodeClient;
import osgi.node.client.example.WSClientException;

/**
 * The node CLI command
 *
 */
public class NodeCommand {

	protected BundleContext bundleContext;
	protected String host;
	protected int port;
	protected String basePath = "/node";
	protected ServiceRegistration<?> nodeCommandReg;

	/**
	 * 
	 * @param bundleContext
	 * @param host
	 * @param port
	 */
	public NodeCommand(BundleContext bundleContext, String host, int port) {
		this.bundleContext = bundleContext;
		this.host = host;
		this.port = port;
	}

	public void start() {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		props.put("osgi.command.scope", "node");
		props.put("osgi.command.function", new String[] { "ping", "version", "shutdown" });
		this.nodeCommandReg = bundleContext.registerService(NodeCommand.class.getName(), this, props);
	}

	public void stop() {
		if (this.nodeCommandReg != null) {
			this.nodeCommandReg.unregister();
			this.nodeCommandReg = null;
		}
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	@Descriptor("ping node server")
	public void ping(@Descriptor("automatically supplied shell session") CommandSession session) {
		try {
			NodeClient nodeClient = new NodeClient(this.host, this.port);
			nodeClient.setBasePath(this.basePath);
			boolean result = nodeClient.ping();

			if (result) {
				System.out.println("ping node: successful");
			} else {
				System.out.println("ping node: failed");
			}
		} catch (WSClientException e) {
			e.printStackTrace();
		}
	}

	@Descriptor("get node server version")
	public void version(@Descriptor("automatically supplied shell session") CommandSession session) {
		try {
			NodeClient nodeClient = new NodeClient(this.host, this.port);
			nodeClient.setBasePath(this.basePath);
			String version = nodeClient.getVersion();

			System.out.println("node version is: " + version);
		} catch (WSClientException e) {
			e.printStackTrace();
		}
	}

	@Descriptor("shut down node server")
	public void shutdown(@Descriptor("automatically supplied shell session") CommandSession session) {
		try {
			NodeClient nodeClient = new NodeClient(this.host, this.port);
			nodeClient.setBasePath(this.basePath);
			String result = nodeClient.shutdown();

			System.out.println("node shutdown: " + result);
		} catch (WSClientException e) {
			e.printStackTrace();
		}
	}

}
