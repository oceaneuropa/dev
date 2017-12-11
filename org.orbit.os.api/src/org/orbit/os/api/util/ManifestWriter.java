package org.orbit.os.api.util;

import static org.orbit.os.api.util.ManifestReader.BUNDLES;
import static org.orbit.os.api.util.ManifestReader.BUNDLE_SYMBOLIC_NAME;
import static org.orbit.os.api.util.ManifestReader.BUNDLE_VERSION;
import static org.orbit.os.api.util.ManifestReader.CLASSPATH;
import static org.orbit.os.api.util.ManifestReader.DESCRIPTION;
import static org.orbit.os.api.util.ManifestReader.ID;
import static org.orbit.os.api.util.ManifestReader.NAME;
import static org.orbit.os.api.util.ManifestReader.PROPERTIES;
import static org.orbit.os.api.util.ManifestReader.PROPERTY_NAME;
import static org.orbit.os.api.util.ManifestReader.PROPERTY_VALUE;
import static org.orbit.os.api.util.ManifestReader.TYPE;
import static org.orbit.os.api.util.ManifestReader.VERSION;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;
import org.orbit.os.api.apps.ProgramManifest;
import org.orbit.os.api.apps.BundleManifest;
import org.origin.common.io.IOUtil;
import org.origin.common.json.JSONUtil;

public class ManifestWriter {

	public ManifestWriter() {
	}

	/**
	 * 
	 * @param manifest
	 * @param file
	 * @throws IOException
	 */
	public void write(ProgramManifest manifest, File file) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			write(manifest, fos);

		} catch (FileNotFoundException e) {
			throw e;

		} finally {
			IOUtil.closeQuietly(fos, true);
		}
	}

	/**
	 * 
	 * @param manifest
	 * @param output
	 * @throws IOException
	 */
	public void write(ProgramManifest manifest, OutputStream output) throws IOException {
		JSONObject rootJSONObject = contentsToDocument(manifest);
		if (rootJSONObject != null) {
			JSONUtil.save(rootJSONObject, output, false);
		}
	}

	/**
	 * 
	 * @param manifest
	 * @return
	 */
	protected JSONObject contentsToDocument(ProgramManifest manifest) {
		JSONObject rootJSONObject = new JSONObject();

		// "id" attribute
		String id = manifest.getId();
		if (id != null) {
			rootJSONObject.put(ID, id);
		}

		// "version" attribute
		String version = manifest.getVersion();
		if (version != null) {
			rootJSONObject.put(VERSION, version);
		}

		// "type" attribute
		String type = manifest.getType();
		if (type != null) {
			rootJSONObject.put(TYPE, type);
		}

		// "name" attribute
		String name = manifest.getName();
		if (name != null) {
			rootJSONObject.put(NAME, name);
		}

		// "description" attribute
		String description = manifest.getDescription();
		if (description != null) {
			rootJSONObject.put(DESCRIPTION, description);
		}

		// "bundles" object array
		BundleManifest[] bundleManifests = manifest.getBundles();
		if (bundleManifests != null) {
			JSONArray bundlesJSONArray = new JSONArray();
			int index = 0;
			for (BundleManifest requireBundle : bundleManifests) {
				String bundleSymbolicName = requireBundle.getSymbolicName();
				String bundleVersion = requireBundle.getVersion();
				if (bundleSymbolicName != null && bundleVersion != null) {
					JSONObject bundleJSONObject = new JSONObject();
					bundleJSONObject.put(BUNDLE_SYMBOLIC_NAME, bundleSymbolicName);
					bundleJSONObject.put(BUNDLE_VERSION, bundleVersion);
					bundlesJSONArray.put(index++, bundleJSONObject);
				}
			}
			rootJSONObject.put(BUNDLES, bundlesJSONArray);
		}

		// "classpath" string array
		String[] classPaths = manifest.getClassPaths();
		if (classPaths != null && classPaths.length > 0) {
			JSONArray classPathJSONArray = new JSONArray();
			for (int i = 0; i < classPaths.length; i++) {
				classPathJSONArray.put(i, classPaths[i]);
			}
			rootJSONObject.put(CLASSPATH, classPathJSONArray);
		}

		// "properties" object array
		Properties properties = manifest.getProperties();
		if (properties != null) {
			JSONArray propertiesJSONArray = new JSONArray();
			int index = 0;
			for (Iterator<String> propNamesItor = properties.stringPropertyNames().iterator(); propNamesItor.hasNext();) {
				String propName = propNamesItor.next();
				String propValue = properties.getProperty(propName);
				if (propName != null && propValue != null) {
					JSONObject propertyJSONObject = new JSONObject();
					propertyJSONObject.put(PROPERTY_NAME, propName);
					propertyJSONObject.put(PROPERTY_VALUE, propValue);
					propertiesJSONArray.put(index++, propertyJSONObject);
				}
			}
			rootJSONObject.put(PROPERTIES, propertiesJSONArray);
		}

		return rootJSONObject;
	}

}

// "main_class" attribute
// String mainClassName = manifest.getMainClassName();
// if (mainClassName != null) {
// rootJSONObject.put("main_class", mainClassName);
// }
