package org.origin.common.rest.client;

import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ClientConfiguration {

	protected static Map<String, ClientConfiguration> configMap = new HashMap<String, ClientConfiguration>();

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @return
	 */
	public static synchronized ClientConfiguration get(String url, String contextRoot, String username) {
		String key = url + "::" + contextRoot + "::" + username;
		ClientConfiguration config = configMap.get(key);
		if (config == null) {
			config = new ClientConfiguration(url, contextRoot);
			config.setUsername(username);
			configMap.put(key, config);
		}
		return config;
	}

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @return
	 */
	public static synchronized ClientConfiguration get(String url, String contextRoot, String username, String password) {
		String key = url + "::" + contextRoot + "::" + username;
		ClientConfiguration config = configMap.get(key);
		if (config == null) {
			config = new ClientConfiguration(url, contextRoot);
			config.setUsername(username);
			if (password != null) {
				config.setPassword(password);
			}
			configMap.put(key, config);
		} else {
			String oldPassword = config.getPassword();
			if ((oldPassword == null && password != null) && (password != null && !password.equals(oldPassword))) {
				// need to reset any cached login result

				config.setPassword(password);
			}
		}
		return config;
	}

	protected static TrustManager[] TRUST = new TrustManager[] { new X509TrustManager() {
		@Override
		public void checkClientTrusted(X509Certificate[] cert, String autyType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] cert, String autyType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	} };

	protected static class TrustAllHostNameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			return true;
		}
	}

	public static final int DEFAULT_PORT = 8090;

	protected String url;
	protected String scheme;
	protected String host;
	protected int port;

	protected String contextRoot;

	protected String username;
	protected String password;

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 */
	public ClientConfiguration(String url, String contextRoot) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("URL is empty.");
		}
		this.url = url;

		URI uri = null;
		try {
			uri = URI.create(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (uri != null) {
			String scheme = uri.getScheme();
			if (scheme == null || scheme.isEmpty() || (!"http".equals(scheme) && !"https".equals(scheme))) {
				scheme = "http";
			}
			this.scheme = scheme;
			this.host = uri.getHost();
			this.port = uri.getPort();
			if (this.port < 0) {
				this.port = DEFAULT_PORT;
			}
		}
		this.contextRoot = contextRoot;
	}

	/**
	 * 
	 * @param scheme
	 * @param host
	 * @param port
	 * @param contextRoot
	 */
	public ClientConfiguration(String scheme, String host, int port, String contextRoot) {
		if (scheme == null || scheme.isEmpty() || (!"http".equals(scheme) && !"https".equals(scheme))) {
			scheme = "http";
		}
		if (host == null || host.isEmpty()) {
			throw new IllegalArgumentException("host is empty.");
		}
		if (this.port < 0) {
			this.port = DEFAULT_PORT;
		}
		this.url = scheme + "://" + host + ":" + port;
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.contextRoot = contextRoot;
	}

	public String getId() {
		String id = this.url + "::" + this.contextRoot + "::" + this.username;
		return id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Client createClient() {
		Client client = null;
		ClientConfig clientCfg = new ClientConfig();
		clientCfg.register(JacksonFeature.class);
		clientCfg.register(MultiPartFeature.class);

		String scheme = getScheme();
		if ("https".equalsIgnoreCase(scheme)) {
			SSLContext sslCtx = null;
			try {
				sslCtx = SSLContext.getInstance("SSL");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			try {
				sslCtx.init(null, TRUST, new SecureRandom());
			} catch (KeyManagementException e) {
				e.printStackTrace();
			}
			client = ClientBuilder.newBuilder().register(MultiPartFeature.class).withConfig(clientCfg).hostnameVerifier(new TrustAllHostNameVerifier()).sslContext(sslCtx).build();
		} else if ("http".equalsIgnoreCase(scheme)) {
			client = ClientBuilder.newBuilder().register(MultiPartFeature.class).withConfig(clientCfg).build();
		} else {
			throw new IllegalArgumentException("URL is invalid.");
		}
		return client;
	}

	/**
	 * @return
	 */
	public String getBaseUrl() {
		String url = scheme + "://" + host + ":" + port;

		String contextRoot = this.contextRoot;
		if (contextRoot != null) {
			contextRoot = contextRoot.trim();
		}

		if (contextRoot == null || contextRoot.isEmpty() || "/".equals(contextRoot)) {
			return url;
		} else {
			//
			if (!contextRoot.startsWith("/")) {
				contextRoot = "/" + contextRoot;
			}
			if (contextRoot.endsWith("/")) {
				contextRoot = contextRoot.substring(0, contextRoot.lastIndexOf("/"));
			}
			return url + contextRoot;
		}
	}

	/**
	 * 
	 * @return
	 */
	public URI getBaseURI() {
		String contextRoot = this.contextRoot;
		if (contextRoot != null) {
			contextRoot = contextRoot.trim();
		}
		if (contextRoot == null || contextRoot.isEmpty()) {
			contextRoot = "/";
		} else {
			if (!contextRoot.startsWith("/")) {
				contextRoot = "/" + contextRoot;
			}
			if (contextRoot.endsWith("/")) {
				contextRoot = contextRoot.substring(0, contextRoot.lastIndexOf("/"));
			}
		}
		return UriBuilder.fromPath(contextRoot).scheme(this.scheme).host(this.host).port(this.port).build();
	}

	/**
	 * 
	 * @param builder
	 * @return
	 */
	public Builder updateHeaders(Builder builder) {
		if (builder != null) {

		}
		return builder;
	}

}
