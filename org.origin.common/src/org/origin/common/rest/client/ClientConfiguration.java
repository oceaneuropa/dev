package org.origin.common.rest.client;

import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ClientConfiguration {

	public static synchronized ClientConfiguration create(String fullUrl) {
		try {
			String hostUrl = null;
			String contextRoot = null;

			URL url = new URL(fullUrl.trim());

			String fullURL = url.toExternalForm();
			String path = url.getPath();

			if (path != null && !path.isEmpty()) {
				hostUrl = fullURL.substring(0, fullURL.indexOf(path));
				contextRoot = path;
			} else {
				hostUrl = fullURL;
			}

			if (hostUrl == null) {
				hostUrl = "";
			}
			if (contextRoot == null) {
				contextRoot = "";
			}

			return new ClientConfiguration(hostUrl, contextRoot);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static synchronized ClientConfiguration get(String hostUrl, String contextRoot, String username, String password) {
		return new ClientConfiguration(hostUrl, contextRoot);
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
	protected String tokenType = "Bearer";
	protected String accessToken = "";

	/**
	 * 
	 * @param url
	 */
	public ClientConfiguration(String url) {
		this(url, null);
	}

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

			String path = uri.getPath();
			if (contextRoot == null) {
				if (path != null) {
					contextRoot = path;
				}
			}

			if (this.port < 0) {
				this.port = DEFAULT_PORT;
			}
		}
		this.contextRoot = contextRoot;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScheme() {
		return this.scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
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

}

// protected static Map<String, ClientConfiguration> configMap = new HashMap<String, ClientConfiguration>();

// protected String username;
// protected String password;

// /**
// *
// * @param url
// * @param contextRoot
// * @param username
// * @return
// */
// public static synchronized ClientConfiguration get(String url, String contextRoot, String username, String password) {
// String key = url + "::" + contextRoot + "::" + username;
// ClientConfiguration config = configMap.get(key);
// if (config == null) {
// config = new ClientConfiguration(url, contextRoot);
// config.setUsername(username);
// if (password != null) {
// config.setPassword(password);
// }
// configMap.put(key, config);
// } else {
// String oldPassword = config.getPassword();
// if ((oldPassword == null && password != null) && (password != null && !password.equals(oldPassword))) {
// // need to reset any cached login result
//
// config.setPassword(password);
// }
// }
// return config;
// }

// public static synchronized ClientConfiguration get(String hostUrl, String contextRoot, String username, String password) {
// ClientConfiguration config = new ClientConfiguration(hostUrl, contextRoot);
// config.setUsername(username);
// config.setPassword(password);
// return config;
// }

// public String getId() {
// String id = this.url + "::" + this.contextRoot + "::" + this.username;
// return id;
// }

// public String getUsername() {
// return username;
// }
//
// public void setUsername(String username) {
// this.username = username;
// }
//
// public String getPassword() {
// return password;
// }
//
// public void setPassword(String password) {
// this.password = password;
// }

/// **
// *
// * @param scheme
// * @param host
// * @param port
// * @param contextRoot
// */
// public ClientConfiguration(String scheme, String host, int port, String contextRoot) {
// if (scheme == null || scheme.isEmpty() || (!"http".equals(scheme) && !"https".equals(scheme))) {
// scheme = "http";
// }
// if (host == null || host.isEmpty()) {
// throw new IllegalArgumentException("host is empty.");
// }
// if (this.port < 0) {
// this.port = DEFAULT_PORT;
// }
// this.url = scheme + "://" + host + ":" + port;
// this.scheme = scheme;
// this.host = host;
// this.port = port;
// this.contextRoot = contextRoot;
// }

// /**
// *
// * @param builder
// * @return
// */
// public Builder updateHeaders(Builder builder) {
// if (builder != null) {
//
// }
// return builder;
// }

// public static void main(String[] args) {
// ClientConfiguration config = new ClientConfiguration("http://127.0.0.1:10001/orbit/v1", null);
// }
