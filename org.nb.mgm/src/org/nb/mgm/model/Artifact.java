package org.nb.mgm.model;

import org.nb.common.rest.model.ModelObject;

public class Artifact extends ModelObject {

	protected String version;
	protected String filePath;
	protected String fileName;
	protected long fileSize;
	protected long checksum;

	public Artifact() {
	}

	/**
	 * 
	 * @param metaSector
	 */
	public Artifact(MetaSector metaSector) {
		super(metaSector);
	}

	public MetaSector getMetaSector() {
		if (this.getParent() instanceof MetaSector) {
			return (MetaSector) this.getParent();
		}
		return null;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public long getChecksum() {
		return checksum;
	}

	public void setChecksum(long checksum) {
		this.checksum = checksum;
	}

}
