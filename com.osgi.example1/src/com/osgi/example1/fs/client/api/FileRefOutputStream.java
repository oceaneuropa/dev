package com.osgi.example1.fs.client.api;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.osgi.example1.util.IOUtil;

/**
 * A file ref output stream that uploads the content of a file on the fly.
 * 
 * <p>
 * The client of this output stream can write the content of a file to it, while the upload keeps reading data from it (through a input that is
 * connected to the output) and uploading the data to remote server.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 * 
 */
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
					// Read file content from a input and upload the file content to remote server. This output is connected to the input.
					// So the client of this output can write data to the output while the upload keeps reading data from the input and uploading.

					// client -> output <- input <- upload
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
