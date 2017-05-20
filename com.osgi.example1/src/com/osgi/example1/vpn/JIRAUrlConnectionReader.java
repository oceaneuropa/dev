package com.osgi.example1.vpn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @see https://docs.oracle.com/javase/tutorial/networking/urls/readingWriting.html
 * 
 */
public class JIRAUrlConnectionReader {

	public static void main(String[] args) {
		try {
			URL url = new URL("https://some.company.com/browse/some-ticket");
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
