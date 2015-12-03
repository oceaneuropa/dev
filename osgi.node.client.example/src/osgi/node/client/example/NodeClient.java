package osgi.node.client.example;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NodeClient extends WSClient {

	/**
	 * 
	 * @param url
	 */
	public NodeClient(String url) {
		super(url);
	}

	/**
	 * 
	 * @param host
	 * @param port
	 */
	public NodeClient(String host, int port) {
		this(host, port, "http");
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param scheme
	 */
	public NodeClient(String host, int port, String scheme) {
		super(host, port, scheme);
	}

	/**
	 * Ping the node.
	 * 
	 * @return
	 * @throws WSClientException
	 */
	public boolean ping() throws WSClientException {
		try {
			Builder builder = wsClient.target(getBaseURL() + "/ping").request(MediaType.APPLICATION_JSON_TYPE);
			Response response = updateHeaders(builder).get();
			checkResponse(response);
			String ping = response.readEntity(String.class);
			try {
				if (Integer.parseInt(ping) > 0) {
					return true;
				}
			} catch (Exception e) {
			}
		} catch (Exception e) {
			handleException(e);
		}
		return false;
	}

	/**
	 * Get node version.
	 * 
	 * @return
	 * @throws WSClientException
	 */
	public String getVersion() throws WSClientException {
		String version = null;
		try {
			Builder builder = getClient().target(getBaseURL() + "/version").request(MediaType.APPLICATION_JSON_TYPE);
			Response response = updateHeaders(builder).get();
			checkResponse(response);
			version = response.readEntity(String.class);

		} catch (Exception e) {
			handleException(e);
		}
		if (version == null) {
			version = "";
		}
		return version;
	}

	/**
	 * Shutdown node.
	 * 
	 * @return
	 * @throws WSClientException
	 */
	public String shutdown() throws WSClientException {
		String shutdown = null;
		try {
			Builder builder = getClient().target(getBaseURL() + "/shutdown").request(MediaType.APPLICATION_JSON_TYPE);
			Response response = updateHeaders(builder).get();
			checkResponse(response);
			shutdown = response.readEntity(String.class);

		} catch (Exception e) {
			handleException(e);
		}
		if (shutdown == null) {
			shutdown = "";
		}
		return shutdown;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		NodeClient nodeClient = new NodeClient("localhost", 9090);
		nodeClient.setBasePath("/node");
		try {
			boolean ping = nodeClient.ping();
			Printer.pl("ping = " + ping);

			Thread.sleep(1000);
			String version = nodeClient.getVersion();
			Printer.pl("version = " + version);

			Thread.sleep(5000);
			String shutdown = nodeClient.shutdown();
			Printer.pl("shutdown = " + shutdown);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
