package org.nb.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class JSONUtil {

	public static Charset getDefaultCharset() {
		return StandardCharsets.UTF_8;
	}

	/**
	 * Load JSONObject from a File.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static JSONObject load(File file) throws IOException {
		JSONObject jsonObject = null;
		InputStream is = null;
		try {
			if (file != null && file.exists()) {
				is = new FileInputStream(file);
			}
			if (is != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();

				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}

				String stringContent = buffer.toString();
				jsonObject = new JSONObject(stringContent);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObject;
	}

	/**
	 * Save JSONObject to a File.
	 * 
	 * @param file
	 * @param jsonObject
	 * @throws IOException
	 */
	public static void save(File file, JSONObject jsonObject) throws IOException {
		String stringContent = jsonObject.toString(4);

		ByteArrayInputStream is = null;
		FileOutputStream fos = null;

		byte[] bytes = stringContent.getBytes(getDefaultCharset());
		is = new ByteArrayInputStream(bytes);

		try {
			if (is != null) {
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				fos.write(bytes);
				fos.flush();
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
