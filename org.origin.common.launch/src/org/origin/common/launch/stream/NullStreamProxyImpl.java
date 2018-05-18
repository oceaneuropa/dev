package org.origin.common.launch.stream;

import java.io.IOException;
import java.io.InputStream;

public class NullStreamProxyImpl implements StreamProxy {

	private NullStreamMonitor outputStreamMonitor;
	private NullStreamMonitor errorStreamMonitor;

	public NullStreamProxyImpl(Process process) {
		this.outputStreamMonitor = new NullStreamMonitor(process.getInputStream());
		this.errorStreamMonitor = new NullStreamMonitor(process.getErrorStream());
	}

	@Override
	public void closeInputStream() throws IOException {
	}

	@Override
	public StreamMonitor getErrorStreamMonitor() {
		return this.errorStreamMonitor;
	}

	@Override
	public StreamMonitor getOutputStreamMonitor() {
		return this.outputStreamMonitor;
	}

	@Override
	public void write(String input) throws IOException {
	}

	private class NullStreamMonitor implements StreamMonitor {
		private InputStream fStream;

		public NullStreamMonitor(InputStream stream) {
			this.fStream = stream;
			startReaderThread();
		}

		private void startReaderThread() {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] bytes = new byte[1024];
					try {
						while (fStream.read(bytes) >= 0) {
							// do nothing
						}
					} catch (IOException e) {
					}
				}
				// }, DebugCoreMessages.NullStreamsProxy_0);
			}, "NullStreamsProxy_0");
			thread.setDaemon(true);
			thread.start();
		}

		@Override
		public void addListener(StreamListener listener) {
		}

		@Override
		public String getContents() {
			return ""; //$NON-NLS-1$
		}

		@Override
		public void removeListener(StreamListener listener) {
		}

		@Override
		public void flushContents() {
			// not supported
		}

		@Override
		public void setBuffered(boolean buffer) {
			// not supported
		}

		@Override
		public boolean isBuffered() {
			// not supported
			return false;
		}
	}

}
