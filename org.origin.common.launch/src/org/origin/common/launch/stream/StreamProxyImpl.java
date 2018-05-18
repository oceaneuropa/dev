package org.origin.common.launch.stream;

import java.io.IOException;

public class StreamProxyImpl implements StreamProxy {

	private InputStreamMonitor inputMonitor;
	private OutputStreamMonitor outputMonitor;
	private OutputStreamMonitor errorMonitor;
	private boolean isClosed;

	public StreamProxyImpl(Process process, String encoding) {
		if (process == null) {
			return;
		}
		this.inputMonitor = new InputStreamMonitor(process.getOutputStream(), encoding);
		this.outputMonitor = new OutputStreamMonitor(process.getInputStream(), encoding);
		this.errorMonitor = new OutputStreamMonitor(process.getErrorStream(), encoding);

		this.inputMonitor.startMonitoring();
		this.outputMonitor.startMonitoring();
		this.errorMonitor.startMonitoring();
	}

	/**
	 * Causes the proxy to close all communications between it and the underlying streams after all remaining data in the streams is read.
	 */
	public void close() {
		if (!isClosed(true)) {
			this.outputMonitor.close();
			this.errorMonitor.close();
			this.inputMonitor.close();
		}
	}

	private synchronized boolean isClosed(boolean newValue) {
		boolean oldValue = this.isClosed;
		this.isClosed = newValue;
		return oldValue;
	}

	/**
	 * Causes the proxy to close all communications between it and the underlying streams immediately. Data remaining in the streams is lost.
	 */
	public void kill() {
		synchronized (this) {
			this.isClosed = true;
		}
		this.outputMonitor.kill();
		this.errorMonitor.kill();
		this.inputMonitor.close();
	}

	@Override
	public StreamMonitor getErrorStreamMonitor() {
		return this.errorMonitor;
	}

	@Override
	public StreamMonitor getOutputStreamMonitor() {
		return this.outputMonitor;
	}

	@Override
	public void write(String input) throws IOException {
		if (!isClosed(false)) {
			this.inputMonitor.write(input);
		} else {
			throw new IOException();
		}
	}

	@Override
	public void closeInputStream() throws IOException {
		if (!isClosed(false)) {
			this.inputMonitor.closeInputStream();
		} else {
			throw new IOException();
		}
	}

}
