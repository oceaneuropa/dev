package org.origin.common.util;

import java.io.File;
import java.util.Iterator;
import java.util.Properties;

public class SystemUtils {

	public static void printProperties() {
		System.out.println();
		System.out.println("System.getProperties()");
		System.out.println("----------------------------------------------------------------------");
		Properties props = System.getProperties();
		for (Iterator<Object> keyItor = props.keySet().iterator(); keyItor.hasNext();) {
			Object key = keyItor.next();
			String value = props.getProperty(key.toString());
			System.out.println(key + "=" + value);
		}
		System.out.println("----------------------------------------------------------------------");
	}

	public static File getUserDir() {
		File dir = null;
		try {
			String path = System.getProperty("user.dir");
			if (path != null) {
				dir = new File(path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dir;
	}

}
