package org.origin.core.resources.internal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.io.IOUtil;
import org.origin.core.resources.IFile;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IRoot;

public class FileImpl extends ResourceImpl implements IFile {

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public FileImpl(IRoot root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public boolean create() throws IOException {
		return getRoot().createUnderlyingFile(getFullPath());
	}

	@Override
	public boolean create(InputStream input) throws IOException {
		return getRoot().createUnderlyingFile(getFullPath(), input);
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return getRoot().getInputStream(getFullPath());
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return getRoot().getOutputStream(getFullPath());
	}

	@Override
	public void setContent(InputStream source) throws IOException {
		if (source != null) {
			OutputStream output = null;
			try {
				output = getOutputStream();
				if (output != null) {
					IOUtil.copy(source, output);
				}
			} finally {
				IOUtil.closeQuietly(output, true);
			}
		}
	}

	@Override
	public void setContent(byte[] bytes) throws IOException {
		setContent(new ByteArrayInputStream(bytes));
	}

}
