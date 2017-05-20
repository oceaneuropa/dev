package org.orbit.fs.connector;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.orbit.fs.api.FileRef;
import org.origin.common.io.IOUtil;

/**
 * A file ref input stream that downloads the content of a file on the fly.
 * 
 * <p>
 * The client of this input stream can read the content of a file from it, while the download keeps downloading the file from a remote server and writing data to it (through a output that is connected
 * to the input).
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
					// Download a remote file and write the file content into a output. This input is connected to the output.
					// So the client of this input can read data from the input while the download keeps downloading and writing data into the output.

					// client -> input -> output <- download
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
