package org.nb.mgm.model.runtime;

import java.util.Date;

import org.origin.common.rest.model.ModelObject;

public class Software extends ModelObject {

	protected String type;
	protected String version;
	protected long length;
	protected Date lastModified;
	protected String md5;

	// for local Admin only (where a Software is stored in file system folder)
	protected String localPath;

	// for local or DB Admin
	protected String fileName;

	public Software() {
	}

	/**
	 * 
	 * @param project
	 */
	public Software(Project project) {
		super(project);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// attributes
	// ----------------------------------------------------------------------------------------------------------------
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static class SoftwareProxy extends Software {
		/**
		 * 
		 * @param softwareId
		 */
		public SoftwareProxy(String softwareId) {
			super();
			setId(softwareId);
		}

		/**
		 * 
		 * @param project
		 * @param softwareId
		 */
		public SoftwareProxy(Project project, String softwareId) {
			super(project);
			setId(softwareId);
		}

		@Override
		public boolean isProxy() {
			return true;
		}
	}

}
