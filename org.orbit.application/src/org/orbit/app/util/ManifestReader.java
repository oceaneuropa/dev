package org.orbit.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.app.AppManifest;
import org.orbit.app.BundleManifest;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;

public class ManifestReader {

	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String BUNDLES = "bundles";
	public static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	public static final String BUNDLE_VERSION = "Bundle-Version";
	public static final String CLASSPATH = "classpath";
	public static final String PROPERTIES = "properties";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_VALUE = "value";

	public ManifestReader() {
	}

	/**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public AppManifest read(File file) throws IOException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return read(fis);

		} catch (FileNotFoundException e) {
			throw e;

		} finally {
			IOUtil.closeQuietly(fis, true);
		}
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public AppManifest read(InputStream input) throws IOException {
		JSONObject rootJSONObject = JSONUtil.load(input, false);
		AppManifest manifest = jsonToManifest(rootJSONObject);
		return manifest;
	}

	/**
	 * 
	 * @param rootJSONObject
	 * @return
	 */
	protected AppManifest jsonToManifest(JSONObject rootJSONObject) {
		if (rootJSONObject == null) {
			return null;
		}

		AppManifest manifest = new AppManifest();

		// "id" attribute
		String id = null;
		if (rootJSONObject.has(ID)) {
			id = rootJSONObject.getString(ID);
		}
		manifest.setId(id);

		// "version" attribute
		String version = null;
		if (rootJSONObject.has(VERSION)) {
			version = rootJSONObject.getString(VERSION);
		}
		manifest.setVersion(version);

		// "type" attribute
		String type = null;
		if (rootJSONObject.has(TYPE)) {
			type = rootJSONObject.getString(TYPE);
		}
		manifest.setType(type);

		// "name" attribute
		String name = null;
		if (rootJSONObject.has(NAME)) {
			name = rootJSONObject.getString(NAME);
		}
		manifest.setName(name);

		// "description" attribute
		String description = null;
		if (rootJSONObject.has(DESCRIPTION)) {
			description = rootJSONObject.getString(DESCRIPTION);
		}
		manifest.setDescription(description);

		// "bundles" object array
		List<BundleManifest> bundleManifests = new ArrayList<BundleManifest>();
		if (rootJSONObject.has(BUNDLES)) {
			JSONArray bundlesJSONArray = rootJSONObject.getJSONArray(BUNDLES);
			if (bundlesJSONArray != null) {
				int length = bundlesJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject bundleJSONObject = bundlesJSONArray.getJSONObject(i);
					if (bundleJSONObject != null) {
						String bundleSymbolicName = null;
						if (bundleJSONObject.has(BUNDLE_SYMBOLIC_NAME)) {
							bundleSymbolicName = bundleJSONObject.getString(BUNDLE_SYMBOLIC_NAME);
						}
						String bundleVersion = null;
						if (bundleJSONObject.has(BUNDLE_VERSION)) {
							bundleVersion = bundleJSONObject.getString(BUNDLE_VERSION);
						}
						if (bundleSymbolicName != null && bundleVersion != null) {
							BundleManifest bundleManifest = new BundleManifest(bundleSymbolicName, bundleVersion);
							bundleManifests.add(bundleManifest);
						}
					}
				}
			}
		}
		manifest.setBundles(bundleManifests.toArray(new BundleManifest[bundleManifests.size()]));

		// "classpath" string array
		List<String> classPaths = new ArrayList<String>();
		if (rootJSONObject.has(CLASSPATH)) {
			JSONArray classPathsJSONArray = rootJSONObject.getJSONArray(CLASSPATH);
			if (classPathsJSONArray != null) {
				int length = classPathsJSONArray.length();
				for (int i = 0; i < length; i++) {
					Object obj = classPathsJSONArray.get(i);
					if (obj instanceof String) {
						String classPath = (String) obj;
						classPaths.add(classPath);
					}
				}
			}
		}
		manifest.setClassPaths(classPaths.toArray(new String[classPaths.size()]));

		// "properties" object array
		Properties properties = new Properties();
		if (rootJSONObject.has(PROPERTIES)) {
			JSONArray propertiesJSONArray = rootJSONObject.getJSONArray(PROPERTIES);
			if (propertiesJSONArray != null) {
				int length = propertiesJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject propertyJSONObject = propertiesJSONArray.getJSONObject(i);
					if (propertyJSONObject != null) {
						String key = null;
						if (propertyJSONObject.has(PROPERTY_NAME)) {
							key = propertyJSONObject.getString(PROPERTY_NAME);
						}
						String value = null;
						if (propertyJSONObject.has(PROPERTY_VALUE)) {
							value = propertyJSONObject.getString(PROPERTY_VALUE);
						}
						if (key != null && value != null) {
							properties.setProperty(key, value);
						}
					}
				}
			}
		}
		manifest.setProperties(properties);

		return manifest;
	}

}

// "main_class" attribute
// String main_class = null;
// if (rootJSONObject.has("main_class")) {
// main_class = rootJSONObject.getString("main_class");
// }
// manifest.setMainClassName(main_class);
