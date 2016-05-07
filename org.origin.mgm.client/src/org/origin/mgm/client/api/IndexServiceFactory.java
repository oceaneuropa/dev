package org.origin.mgm.client.api;

import java.net.MalformedURLException;
import java.net.URL;

import org.origin.mgm.client.api.impl.IndexServiceImpl;

public class IndexServiceFactory {

	/**
	 * @param url
	 *            e.g. http://127.0.0.1:9090/indexservice/v1
	 * @param username
	 *            admin
	 * @param password
	 *            admin
	 * @return
	 */
	public static IndexService createIndexService(String url, String username, String password) {
		String theUrl = null;
		String contextRoot = null;
		URL urL = null;
		try {
			urL = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (urL != null) {
			String protocol = urL.getProtocol();
			String authority = urL.getAuthority();
			theUrl = protocol + "://" + authority;
			contextRoot = urL.getPath();
		} else {
			theUrl = url;
			contextRoot = null;
		}
		return createIndexService(theUrl, contextRoot, username, password);
	}

	/**
	 * @param url
	 *            e.g. http://127.0.0.1:9090
	 * @param contextRoot
	 *            e.g. /indexservice/v1
	 * @param username
	 *            admin
	 * @param password
	 *            admin
	 * @return
	 */
	public static IndexService createIndexService(String url, String contextRoot, String username, String password) {
		return new IndexServiceImpl(url, contextRoot, username, password);
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String urlString = "http://127.0.0.1:9090/indexservice/v1";

			URL url = new URL(urlString);
			String protocol = url.getProtocol();
			String authority = url.getAuthority();
			String host = url.getHost();
			int port = url.getPort();
			String path = url.getPath();
			String query = url.getQuery();
			String file = url.getFile();
			String ref = url.getRef();
			String userInfo = url.getUserInfo();

			System.out.println(urlString);
			System.out.println("protocol=" + protocol);
			System.out.println("authority=" + authority);
			System.out.println("host=" + host);
			System.out.println("port=" + port);
			System.out.println("path=" + path);
			System.out.println("query=" + query);
			System.out.println("file=" + file);
			System.out.println("ref=" + ref);
			System.out.println("userInfo=" + userInfo);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
