package org.orbit.fs.server.googledrive;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.orbit.fs.common.FileSystemConfiguration;

public class GoogleDriveFSConfig extends FileSystemConfiguration {

	// dir to store user credentials
	protected File dataStoreDir;
	// client secret file
	protected File clientSecretFile;
	// accessibility
	protected List<String> scopes;
	// application name
	protected String applicationName;

	public GoogleDriveFSConfig() {
	}

	public File getDataStoreFile() {
		return dataStoreDir;
	}

	public void setDataStoreDir(File dataStoreDir) {
		this.dataStoreDir = dataStoreDir;
	}

	public File getClientSecretDir() {
		return this.clientSecretFile;
	}

	public void setClientSecretFile(File clientSecretFile) {
		this.clientSecretFile = clientSecretFile;
	}

	public List<String> getScopes() {
		return this.scopes;
	}

	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public String toString() {
		String scopesStr = scopes != null ? Arrays.toString(scopes.toArray(new String[scopes.size()])) : "[]";
		// return "GoogleDriveFSConfig [dataStoreDir=" + dataStoreDir + ", clientSecretFile=" + clientSecretFile + ", scopes=" + scopesStr + ", applicationName=" + applicationName + "]";
		StringBuilder sb = new StringBuilder();
		sb.append("GoogleDriveFSConfig [\r\n");
		sb.append("    dataStoreDir=").append(dataStoreDir).append(",\r\n");
		sb.append("    clientSecretFile=").append(clientSecretFile).append(",\r\n");
		sb.append("    scopes=").append(scopesStr).append(",\r\n");
		sb.append("    applicationName=").append(applicationName).append("\r\n");
		sb.append("]\r\n");
		return sb.toString();
	}

}
