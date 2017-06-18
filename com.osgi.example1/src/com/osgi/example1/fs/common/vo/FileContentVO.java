package com.osgi.example1.fs.common.vo;

public class FileContentVO {

	protected int fileContentId;
	protected int fileId;

	public FileContentVO() {
	}

	public FileContentVO(int fileContentId, int fileId) {
		this.fileContentId = fileContentId;
		this.fileId = fileId;
	}

	public int getFileContentId() {
		return fileContentId;
	}

	public void setFileContentId(int fileContentId) {
		this.fileContentId = fileContentId;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("FileContentVO(");
		sb.append("fileContentId=").append(this.fileContentId);
		sb.append(", fileId=").append(this.fileId);
		sb.append(")");
		return sb.toString();
	}

}
