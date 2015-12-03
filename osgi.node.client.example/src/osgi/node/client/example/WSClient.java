package osgi.node.client.example;

import java.net.ConnectException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class WSClient {

	protected static TrustManager[] TRUST = new TrustManager[] { new X509TrustManager() {
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}
	} };

	protected class TrustAllHostNameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	protected String host;
	protected int port;
	protected String basePath;
	protected String scheme;
	protected Client wsClient;

	/**
	 * 
	 * @param url
	 */
	public WSClient(String url) {
		try {
			URI uri = URI.create(url);
			if (uri != null) {
				// host
				this.host = uri.getHost();

				// port
				this.port = uri.getPort();
				if (this.port < 0) {
					this.port = 9090;
				}

				// scheme
				this.scheme = uri.getScheme();
				if (this.scheme == null || this.scheme.trim().isEmpty() || (!"http".equals(this.scheme) && !"https".equals(this.scheme))) {
					this.scheme = "http";
				}

				init();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param host
	 * @param port
	 * @param scheme
	 */
	public WSClient(String host, int port, String scheme) {
		this.host = host;
		this.port = port;
		this.scheme = scheme;

		init();
	}

	protected void init() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);
		clientConfig.register(MultiPartFeature.class);

		if (scheme.equalsIgnoreCase("https")) {
			SSLContext ctx = null;
			try {
				ctx = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			try {
				ctx.init(null, TRUST, new SecureRandom());
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
			wsClient = ClientBuilder.newBuilder().register(MultiPartFeature.class).withConfig(clientConfig).hostnameVerifier(new TrustAllHostNameVerifier()).sslContext(ctx).build();
		} else if (scheme.equalsIgnoreCase("http")) {
			wsClient = ClientBuilder.newBuilder().register(MultiPartFeature.class).withConfig(clientConfig).build();
		} else {
			throw new IllegalArgumentException("url is not valid");
		}
	}

	public Client getClient() {
		return wsClient;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getURL() {
		return scheme + "://" + host + ":" + port;
	}

	public String getBaseURL() {
		String url = getURL();
		String basePath = getBasePath();
		if (basePath != null && !basePath.isEmpty()) {
			// make sure the basePath starts with "/"
			if (!basePath.startsWith("/")) {
				basePath = "/" + basePath;
			}
			url += basePath;
		}
		return url;
	}

	/**
	 * 
	 * @param builder
	 * @return
	 */
	protected Builder updateHeaders(Builder builder) {
		return builder;
	}

	/**
	 * 
	 * @param response
	 * @throws WSClientException
	 */
	protected void checkResponse(Response response) throws WSClientException {
		if (response != null && response.getStatusInfo() != null) {
			if (!Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
				throw new WSClientException(response.getStatus(), response.getStatusInfo().getReasonPhrase(), null);
			}
		}
	}

	/**
	 * 
	 * @param e
	 * @throws WSClientException
	 */
	protected void handleException(Exception e) throws WSClientException {
		if (e == null) {
			return;
		}
		if (e instanceof WSClientException) {
			WSClientException ce = (WSClientException) e;
			throw ce;

		} else if (e instanceof ProcessingException) {
			ProcessingException pe = (ProcessingException) e;
			if (pe.getCause() instanceof ConnectException) {
				throw new WSClientException(503, pe.getCause().getMessage(), pe.getCause());
			}
			throw new WSClientException(500, pe.getMessage(), pe);

		} else {
			throw new WSClientException(500, e.getMessage(), e);
		}
	}

	public void close() {
		wsClient.close();
	}

}
