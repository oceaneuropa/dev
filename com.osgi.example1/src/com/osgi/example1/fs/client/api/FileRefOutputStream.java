package com.osgi.example1.fs.client.api;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.osgi.example1.util.IOUtil;

public class FileRefOutputStream extends PipedOutputStream {

	private FileRef fileRef;
	private PipedInputStream input;

	/**
	 * 
	 * @param fileRef
	 * @throws IOException
	 */
	public FileRefOutputStream(FileRef fileRef) throws IOException {
		if (fileRef == null) {
			throw new IllegalArgumentException("fileRef is null.");
		}
		this.fileRef = fileRef;
		this.input = new PipedInputStream();
		connect(this.input);

		new Thread() {
			@Override
			public void run() {
				try {
					FileRefOutputStream.this.fileRef.getFileSystem().uploadInputStreamToFsFile(FileRefOutputStream.this.input, FileRefOutputStream.this.fileRef);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtil.closeQuietly(FileRefOutputStream.this.input, true);
				}
			}
		}.start();
	}

}
