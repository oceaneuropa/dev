package org.nb.drive.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nb.drive.api.IFile;
import org.nb.drive.rest.dto.FileDTO;

public class RFile implements IFile {

	protected RFile parent;
	protected FileDTO fileDTO;

	/**
	 * 
	 * @param parent
	 * @param fileDTO
	 */
	public RFile(RFile parent, FileDTO fileDTO) {
		this.parent = parent;
		this.fileDTO = fileDTO;
	}

	@Override
	public IFile getParent() {
		return this.parent;
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
		// REST call
		return null;
	}

	@Override
	public void mkdirs() {
		// REST call
	}

	@Override
	public boolean createNewFile() {
		// REST call
		return false;
	}

	@Override
	public boolean delete() {
		// REST call
		return false;
	}

	@Override
	public void write(OutputStream output) throws IOException {

	}

	@Override
	public InputStream read() {
		// REST call to get input stream of remote file.
		return null;
	}

	@Override
	public void copyFrom(File file) {

	}

	@Override
	public void copyTo(File file) {

	}

}
