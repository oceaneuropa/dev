package jersey.example1;

public class HttpConnector {

	private final String scheme;
	private final String address;
	private final int port;
	private final boolean secure;

	public HttpConnector(final String scheme, final String address, final int port, final boolean secure) {
		this.scheme = scheme;
		this.address = address;
		this.port = port;
		this.secure = secure;
	}

	public String getScheme() {
		return scheme;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public boolean isSecure() {
		return secure;
	}

}