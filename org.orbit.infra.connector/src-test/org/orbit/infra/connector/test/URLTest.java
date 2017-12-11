package org.orbit.infra.connector.test;

import java.net.MalformedURLException;
import java.net.URL;

public class URLTest {

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
