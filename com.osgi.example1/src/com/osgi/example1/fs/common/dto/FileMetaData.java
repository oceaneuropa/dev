package com.osgi.example1.fs.common.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for File meta data.
 *
 */
@XmlRootElement
public class FileMetaData {

	@XmlElement
	protected String name;
	@XmlElement
	protected boolean isDirectory;
	@XmlElement
	protected boolean isHidden;
	// @XmlElement
	// protected String absolutePath;
	@XmlElement
	protected String path;
	@XmlElement
	protected String parentPath;
	@XmlElement
	protected boolean exists;
	@XmlElement
	protected boolean canExecute;
	@XmlElement
	protected boolean canRead;
	@XmlElement
	protected boolean canWrite;
	@XmlElement
	protected long length;
	@XmlElement
	protected long lastModified;

	public FileMetaData() {
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public boolean isDirectory() {
		return isDirectory;
	}

	public void setIsDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	@XmlElement
	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	// @XmlElement
	// public String getAbsolutePath() {
	// return absolutePath;
	// }
	//
	// public void setAbsolutePath(String absolutePath) {
	// this.absolutePath = absolutePath;
	// }

	@XmlElement
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlElement
	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	@XmlElement
	public boolean exists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	@XmlElement
	public boolean canExecute() {
		return canExecute;
	}

	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	@XmlElement
	public boolean canRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	@XmlElement
	public boolean canWrite() {
		return canWrite;
	}

	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	@XmlElement
	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@XmlElement
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(name=").append(this.name);
		sb.append(", isDirectory=").append(this.isDirectory);
		sb.append(", isHidden=").append(this.isHidden);
		// sb.append(", absolutePath=").append(this.absolutePath);
		sb.append(", path=").append(this.path);
		sb.append(", parentPath=").append(this.parentPath);
		sb.append(", exists=").append(this.exists);
		sb.append(", canExecute=").append(this.canExecute);
		sb.append(", canRead=").append(this.canRead);
		sb.append(", canWrite=").append(this.canWrite);
		sb.append(", length=").append(this.length);
		sb.append(", lastModified=").append(this.lastModified);
		sb.append(")");

		return sb.toString();
	}

}
