package org.nb.drive.rest.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for File
 *
 */
@XmlRootElement
public class FileDTO {

	@XmlElement
	protected String absolutePath;

	@XmlElement
	protected String path;

	@XmlElement
	protected String fileName;

	@XmlElement
	protected String fileExtension;

	@XmlElement
	protected boolean exist;

	@XmlElement
	protected boolean isDirectory;

	@XmlElement
	protected boolean isFile;

	@XmlElement
	protected long size;

	@XmlElement
	protected long lastModified;

	@XmlElement
	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	@XmlElement
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlElement
	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@XmlElement
	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	@XmlElement
	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@XmlElement
	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	@XmlElement
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@XmlElement
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
