package org.origin.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

}
