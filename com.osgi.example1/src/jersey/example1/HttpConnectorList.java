package jersey.example1;

import java.util.List;

public class HttpConnectorList {

	private final List<HttpConnector> connectors;

	public HttpConnectorList(final List<HttpConnector> connectors) {
		this.connectors = connectors;
	}

	public List<HttpConnector> getConnectors() {
		return connectors;
	}

}