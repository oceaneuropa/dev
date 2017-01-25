package org.orbit.fs.model.vo;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class FileMetadataVO {

	protected int fileId;
	protected int parentFileId;
	protected String name;
	protected boolean isDirectory;
	protected boolean isHidden;
	protected boolean canExecute;
	protected boolean canRead;
	protected boolean canWrite;
	protected long length;
	protected long lastModified;

	public FileMetadataVO() {
	}

	/**
	 * 
	 * @param fileId
	 * @param parentFileId
	 * @param name
	 * @param isDirectory
	 * @param isHidden
	 * @param canExecute
	 * @param canRead
	 * @param canWrite
	 * @param length
	 * @param lastModified
	 */
	public FileMetadataVO(int fileId, int parentFileId, String name, boolean isDirectory, boolean isHidden, boolean canExecute, boolean canRead, boolean canWrite, long length, long lastModified) {
		this.fileId = fileId;
		this.parentFileId = parentFileId;
		this.name = name;
		this.isDirectory = isDirectory;
		this.isHidden = isHidden;
		this.canExecute = canExecute;
		this.canRead = canRead;
		this.canWrite = canWrite;
		this.length = length;
		this.lastModified = lastModified;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public int getParentFileId() {
		return parentFileId;
	}

	public void setParentFileId(int parentFileId) {
		this.parentFileId = parentFileId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean canExecute() {
		return canExecute;
	}

	public void setCanExecute(boolean canExecute) {
		this.canExecute = canExecute;
	}

	public boolean canRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FileMetaDataVO(");
		sb.append("fileId=").append(this.fileId);
		sb.append(", parentFileId=").append(this.parentFileId);
		sb.append(", name=").append(this.name);
		sb.append(", isDirectory=").append(this.isDirectory);
		sb.append(", isHidden=").append(this.isHidden);
		sb.append(", canExecute=").append(this.canExecute);
		sb.append(", canRead=").append(this.canRead);
		sb.append(", canWrite=").append(this.canWrite);
		sb.append(", length=").append(this.length);
		sb.append(", lastModified=").append(this.lastModified);
		sb.append(")");
		return sb.toString();
	}

}
