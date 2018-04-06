package org.origin.common.resources.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.io.IOUtil;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IWorkspace;

public class FileImpl extends ResourceImpl implements IFile {

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public FileImpl(IWorkspace root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public boolean create() throws IOException {
		return getWorkspace().createUnderlyingFile(this);
	}

	@Override
	public boolean create(InputStream input) throws IOException {
		return getWorkspace().createUnderlyingFile(this, input);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return getWorkspace().getInputStream(getFullPath());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return getWorkspace().getOutputStream(getFullPath());
	}

	@Override
	public void setContents(InputStream input) throws IOException {
		if (input != null) {
			OutputStream output = null;
			try {
				output = getOutputStream();
				if (output != null) {
					IOUtil.copy(input, output);
				}
			} finally {
				IOUtil.closeQuietly(output, true);
			}
		}
	}

	@Override
	public void setContents(byte[] bytes) throws IOException {
		setContents(new ByteArrayInputStream(bytes));
	}

}
