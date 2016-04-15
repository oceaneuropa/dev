package org.nb.drive.hdfs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.nb.drive.api.IFile;

public class HdfsFileImpl implements IFile {

	protected FileSystem fs;
	protected Path path;
	protected FileStatus file;

	/**
	 * 
	 * @param fs
	 * @param path
	 */
	public HdfsFileImpl(FileSystem fs, Path path) {
		this.fs = fs;
		this.path = path;
		try {
			this.file = fs.getFileStatus(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fs
	 * @param file
	 */
	public HdfsFileImpl(FileSystem fs, FileStatus file) {
		this.fs = fs;
		this.file = file;
		this.path = file.getPath();
	}

	@Override
	public IFile getParent() {
		return null;
	}

	@Override
	public String getAbsolutePath() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public String getFileName() {
		return null;
	}

	@Override
	public String getFileExtension() {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isFile() {
		return false;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public long lastModified() {
		return 0;
	}

	@Override
	public IFile[] listFiles() {
		return null;
	}

	@Override
	public void mkdirs() {

	}

	@Override
	public boolean createNewFile() {
		return false;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public void write(OutputStream output) throws IOException {

	}

	@Override
	public InputStream read() {
		return null;
	}

	@Override
	public void copyFrom(File file) {

	}

	@Override
	public void copyTo(File file) {

	}

}
