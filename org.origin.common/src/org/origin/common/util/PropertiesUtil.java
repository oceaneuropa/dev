package org.origin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.origin.common.io.IOUtil;

public class PropertiesUtil {

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static Properties getPropertiesFromFile(File file) throws IOException {
		Properties properties = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			properties = new Properties();
			properties.load(fis);
		} finally {
			IOUtil.closeQuietly(fis, true);
		}
		return properties;
	}

	/**
	 * 
	 * @param fname
	 * @return
	 * @throws IOException
	 */
	public static Properties getPropertiesFromFile(String fname) throws IOException {
		Properties properties = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fname);
			properties = new Properties();
			properties.load(fis);
		} finally {
			IOUtil.closeQuietly(fis, true);
		}
		return properties;
	}

	/**
	 * @param configFile
	 * @param config
	 * @throws Exception
	 */
	public static void loadConfigFromFile(final Path configFile, final Map<String, String> config) throws Exception {
		try (Scanner scanner = new Scanner(configFile, StandardCharsets.UTF_8.name())) {
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (line.trim().startsWith("#") || "".equals(line.trim())) {
					continue;
				}

				String[] pair = line.split("=", 2);
				if (pair != null && pair.length == 2) {
					String value = "";
					if (pair[1] != null && !"".equals(pair[1].trim())) {
						value = resolveVariables(pair[1]);
					}
					config.put(pair[0], value);
				}
			}
		}
	}

	/**
	 * Resolve all variables in the given location following the pattern %name%. First look for System property "name", then environment variable "name".
	 *
	 * @param location
	 *            String that may contain variables
	 * @return resolved location String
	 */
	public static String resolveVariables(final String location) {
		Pattern p = Pattern.compile("\\%(.+?)\\%");
		Matcher m = p.matcher(location);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String var = m.group(1);
			String val = null;

			if (var.equalsIgnoreCase("HOSTADDRESS")) {
				try {
					val = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
				}
			}

			if (val == null) {
				val = System.getProperty(var);
			}

			if (val == null) {
				val = System.getenv(var);
			}

			if (val == null) {
				val = "";
			}
			m.appendReplacement(sb, "");
			sb.append(val);
		}
		m.appendTail(sb);
		return sb.toString();
	}

}
