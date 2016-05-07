package com.osgi.example1.fs.client.api;

public interface FileRef {

	public String getPath();

	public String getParent();

	public FileRef getParentFile();

	public String getName();

	public boolean isDirectory();

	public boolean isHidden();

	public boolean exists();

	public boolean canExecute();

	public boolean canRead();

	public boolean canWrite();

	public long getLength();

	public long getLastModified();

}
