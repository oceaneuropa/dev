package org.orbit.os.api.apps;

public class BundleManifest {

	protected String symbolicName;
	protected String version;

	public BundleManifest() {
	}

	public BundleManifest(String symbolicName, String version) {
		this.symbolicName = symbolicName;
		this.version = version;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "BundleManifest [symbolicName=" + symbolicName + ", version=" + version + "]";
	}

}
