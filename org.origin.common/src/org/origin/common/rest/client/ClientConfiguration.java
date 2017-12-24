package org.origin.common.rest.client;

import java.net.URI;
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

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class ClientConfiguration {

	public static final String REALM = "realm";
	public static final String USERNAME = "username";
	public static final String URL = "url";

	public static final int DEFAULT_PORT = 80;
	public static final String DEFAULT_REALM = "default_realm";
	public static final String UNKNOWN_USERNAME = "unknown_user";

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

	public static synchronized ClientConfiguration create(String realm, String username, String fullUrl) {
		return new ClientConfiguration(realm, username, fullUrl, null);
	}

	public static synchronized ClientConfiguration create(String realm, String username, String hostUrl, String contextRoot) {
		return new ClientConfiguration(realm, username, hostUrl, contextRoot);
	}

	protected String realm;
	protected String username;
	protected String scheme;
	protected String host;
	protected int port;
	protected String contextRoot;

	/**
	 * 
	 * @param realm
	 * @param username
	 * @param url
	 * @param contextRoot
	 */
	public ClientConfiguration(String realm, String username, String url, String contextRoot) {
		if (url == null || url.isEmpty()) {
			throw new IllegalArgumentException("URL is empty.");
		}
		this.realm = checkRealm(realm);
		this.username = checkUsername(username);

		URI uri = null;
		try {
			uri = URI.create(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (uri != null) {
			String scheme = uri.getScheme();
			this.scheme = checkScheme(scheme);
			this.host = uri.getHost();
			this.port = uri.getPort();

			String path = uri.getPath();
			if (contextRoot == null && path != null) {
				contextRoot = path;
			}
			if (this.port < 0) {
				this.port = DEFAULT_PORT;
			}
		}
		this.contextRoot = contextRoot;
	}

	protected String checkRealm(String realm) {
		if (realm == null) {
			realm = DEFAULT_REALM;
		}
		return realm;
	}

	protected String checkUsername(String username) {
		if (username == null) {
			username = UNKNOWN_USERNAME;
		}
		return username;
	}

	protected String checkScheme(String scheme) {
		if (scheme == null || scheme.isEmpty() || (!"http".equals(scheme) && !"https".equals(scheme))) {
			scheme = "http";
		}
		return scheme;
	}

	public synchronized String getRealm() {
		return this.realm;
	}

	public synchronized void setRealm(String realm) {
		this.realm = checkRealm(realm);
	}

	public synchronized String getUsername() {
		return this.username;
	}

	public synchronized void setUsername(String username) {
		this.username = checkUsername(username);
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getContextRoot() {
		return this.contextRoot;
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

// public Map<String, String> getProperties() {
// return this.properties;
// }
//
// public void setProperties(Map<String, String> properties) {
// this.properties = properties;
// }

// try {
// String hostUrl = null;
// String contextRoot = null;
//
// URL url = new URL(fullUrl.trim());
//
// String fullURL = url.toExternalForm();
// String path = url.getPath();
//
// if (path != null && !path.isEmpty()) {
// hostUrl = fullURL.substring(0, fullURL.indexOf(path));
// contextRoot = path;
// } else {
// hostUrl = fullURL;
// }
//
// if (hostUrl == null) {
// hostUrl = "";
// }
// if (contextRoot == null) {
// contextRoot = "";
// }

// public URI getBaseURI() {
// String contextRoot = this.contextRoot;
// if (contextRoot != null) {
// contextRoot = contextRoot.trim();
// }
// if (contextRoot == null || contextRoot.isEmpty()) {
// contextRoot = "/";
// } else {
// if (!contextRoot.startsWith("/")) {
// contextRoot = "/" + contextRoot;
// }
// if (contextRoot.endsWith("/")) {
// contextRoot = contextRoot.substring(0, contextRoot.lastIndexOf("/"));
// }
// }
// return UriBuilder.fromPath(contextRoot).scheme(this.scheme).host(this.host).port(this.port).build();
// }
