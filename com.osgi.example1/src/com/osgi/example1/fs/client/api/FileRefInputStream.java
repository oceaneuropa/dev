package com.osgi.example1.fs.client.api;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.osgi.example1.util.IOUtil;

/**
 * A file ref input stream that downloads the content of a file on the fly.
 * 
 */
public class FileRefInputStream extends PipedInputStream {

	private FileRef fileRef;
	private PipedOutputStream output;

	/**
	 * 
	 * @param fileRef
	 * @throws IOException
	 */
	public FileRefInputStream(FileRef fileRef) throws IOException {
		if (fileRef == null) {
			throw new IllegalArgumentException("fileRef is null.");
		}
		this.fileRef = fileRef;
		this.output = new PipedOutputStream();
		connect(this.output);

		new Thread() {
			@Override
			public void run() {
				try {
					FileRefInputStream.this.fileRef.getFileSystem().downloadFsFileToOutputStream(FileRefInputStream.this.fileRef, FileRefInputStream.this.output);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					IOUtil.closeQuietly(FileRefInputStream.this.output, true);
				}
			}
		}.start();
	}

}
