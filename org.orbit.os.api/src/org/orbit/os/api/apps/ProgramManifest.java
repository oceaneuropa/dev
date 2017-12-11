package org.orbit.os.api.apps;

import java.util.Arrays;
import java.util.Properties;

/*
 * Manifest model for the manifest.json file.
 * 
 * "org.abc.editor_1.0.0.zip" app
 * 
 * file content example:
 * --------------------------------------------------------
 * /lib
 * 	    com.editor.lib1.jar
 *      com.editor.lib2.jar
 * manifest.json
 * --------------------------------------------------------
 * 
 * manifest.json example:
 * --------------------------------------------------------
 * {
 * 		"id" : "org.abc.editor", 															// app id
 * 		"version" : "1.0.0",																// app version
 * 		"type" : "editor",																	// type
 * 		"name" : "ABC editor",																// name
 * 		"description" : "simple text editor",												// description
 * 		"bundles" : [																		// bundles of the app
 * 			{"Bundle-SymbolicName" : "com.editor.lib1", "Bundle-Version" : "1.0.1"},
 * 			{"Bundle-SymbolicName" : "com.editor.lib2", "Bundle-Version" : "2.0.1"},
 * 		],
 * 		"classpath" : [																		// classpath of the app
 * 			"/lib/com.editor.lib1.jar",
 * 			"/lib/com.editor.lib2.jar"
 *  	],
 *  	"properties": [																		// properties of the app
 *  		{ "name" : "p1", "value" : "v1"},
 *  		{ "name" : "p2", "value" : "v2"},
 *  	]
 * }
 * --------------------------------------------------------
 * 
 */
public class ProgramManifest {

	protected String id;
	protected String version;
	protected String type;
	protected String name;
	protected String description;
	protected BundleManifest[] modules;
	protected String[] classPaths;
	protected Properties properties;

	public ProgramManifest() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public synchronized BundleManifest[] getBundles() {
		if (this.modules == null) {
			this.modules = new BundleManifest[0];
		}
		return this.modules;
	}

	public void setBundles(BundleManifest[] modules) {
		this.modules = modules;
	}

	public synchronized String[] getClassPaths() {
		if (this.classPaths == null) {
			this.classPaths = new String[0];
		}
		return this.classPaths;
	}

	public void setClassPaths(String[] classPaths) {
		this.classPaths = classPaths;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProgramManifest other = (ProgramManifest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public String getSimpleName() {
		return this.id + " (" + this.version + ")";
	}

	@Override
	public String toString() {
		String bundlesString = this.modules != null ? Arrays.toString(this.modules) : "null";
		String classPathsString = (this.classPaths != null) ? Arrays.toString(this.classPaths) : "null";
		return "AppManifest [id=" + id + ", version=" + version + ", type=" + type + ", name=" + name + ", description=" + description + ", bundles=" + bundlesString + ", classpath=" + classPathsString + ", properties=" + properties + "]";
	}

}
