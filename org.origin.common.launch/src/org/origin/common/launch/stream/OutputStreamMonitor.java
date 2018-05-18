package org.origin.common.launch.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.origin.common.event.ListenerList;
import org.origin.common.launch.util.SafeRunnable;

public class OutputStreamMonitor implements StreamMonitor {

	protected static final int BUFFER_SIZE = 8192;

	protected InputStream inputStream;
	protected String encoding;
	protected StringBuffer contents;
	protected Thread thread;
	protected boolean isBuffered = true;
	protected boolean isKilled = false;
	protected long lastSleep;
	protected ListenerList listeners = new ListenerList();

	/**
	 * 
	 * @param stream
	 * @param encoding
	 */
	public OutputStreamMonitor(InputStream stream, String encoding) {
		this.inputStream = new BufferedInputStream(stream, 8192);
		this.encoding = encoding;
		this.contents = new StringBuffer();
	}

	@Override
	public synchronized void addListener(StreamListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Causes the monitor to close all communications between it and the underlying stream by waiting for the thread to terminate.
	 */
	public void close() {
		if (this.thread != null) {
			Thread theThread = this.thread;
			this.thread = null;
			try {
				theThread.join();
			} catch (InterruptedException ie) {
			}
			this.listeners = new ListenerList();
		}
	}

	/**
	 * Notifies the listeners that text has been appended to the stream.
	 * 
	 * @param text
	 *            the text that was appended to the stream
	 */
	private void fireStreamAppended(String text) {
		getNotifier().notifyAppend(text);
	}

	private ContentNotifier getNotifier() {
		return new ContentNotifier();
	}

	@Override
	public synchronized String getContents() {
		return this.contents.toString();
	}

	/**
	 * Continually reads from the stream.
	 * 
	 */
	private void read() {
		this.lastSleep = System.currentTimeMillis();
		long currentTime = this.lastSleep;
		byte[] bytes = new byte[BUFFER_SIZE];
		int read = 0;
		while (read >= 0) {
			try {
				if (this.isKilled) {
					break;
				}
				read = this.inputStream.read(bytes);
				if (read > 0) {
					String text;
					if (this.encoding != null) {
						text = new String(bytes, 0, read, this.encoding);
					} else {
						text = new String(bytes, 0, read);
					}
					System.out.println(getClass().getName() + ".read() text = " + text);

					synchronized (this) {
						if (isBuffered()) {
							this.contents.append(text);
						}
						fireStreamAppended(text);
					}
				}

			} catch (IOException ioe) {
				if (!isKilled) {
					// DebugPlugin.log(ioe);
					ioe.printStackTrace();
				}
				return;
			} catch (NullPointerException e) {
				// killing the stream monitor while reading can cause an NPE
				// when reading from the stream
				if (!isKilled && thread != null) {
					// DebugPlugin.log(e);
					e.printStackTrace();
				}
				return;
			}

			currentTime = System.currentTimeMillis();
			if (currentTime - lastSleep > 1000) {
				lastSleep = currentTime;
				try {
					Thread.sleep(1); // just give up CPU to maintain UI responsiveness.
				} catch (InterruptedException e) {
				}
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			// DebugPlugin.log(e);
			e.printStackTrace();
		}
	}

	public void kill() {
		isKilled = true;
	}

	@Override
	public synchronized void removeListener(StreamListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Starts a thread which reads from the stream
	 */
	public void startMonitoring() {
		if (this.thread == null) {
			this.thread = new Thread(new Runnable() {
				@Override
				public void run() {
					read();
				}
				// }, DebugCoreMessages.OutputStreamMonitor_label);
			}, "OutputStreamMonitor_label");
			this.thread.setDaemon(true);
			this.thread.setPriority(Thread.MIN_PRIORITY);
			this.thread.start();
		}
	}

	// @Override
	public synchronized void setBuffered(boolean buffer) {
		isBuffered = buffer;
	}

	// @Override
	public synchronized void flushContents() {
		contents.setLength(0);
	}

	// @Override
	public synchronized boolean isBuffered() {
		return isBuffered;
	}

	class ContentNotifier implements SafeRunnable {
		private StreamListener listener;
		private String text;

		public void handleException(Throwable exception) {
			// DebugPlugin.log(exception);
			exception.printStackTrace();
		}

		public void run() throws Exception {
			this.listener.streamAppended(this.text, OutputStreamMonitor.this);
		}

		public void notifyAppend(String text) {
			if (text == null) {
				return;
			}
			this.text = text;
			Object[] copiedListeners = listeners.getListeners();
			for (int i = 0; i < copiedListeners.length; i++) {
				this.listener = (StreamListener) copiedListeners[i];
				SafeRunner.run(this);
			}
			this.listener = null;
			this.text = null;
		}
	}

}
