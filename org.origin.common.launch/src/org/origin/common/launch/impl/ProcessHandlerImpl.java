package org.origin.common.launch.impl;

import java.io.IOException;
import java.util.Map;

import org.origin.common.launch.LaunchHandler;
import org.origin.common.launch.ProcessHandler;
import org.origin.common.launch.stream.NullStreamProxyImpl;
import org.origin.common.launch.stream.StreamProxy;
import org.origin.common.launch.stream.StreamProxyImpl;

public class ProcessHandlerImpl implements ProcessHandler {

	private static final int MAX_WAIT_FOR_DEATH_ATTEMPTS = 10;
	private static final int TIME_TO_WAIT_FOR_THREAD_DEATH = 500; // ms

	protected LaunchHandler launch;
	protected Process process;
	protected String label;
	protected Map<String, String> attributes;
	protected boolean terminated;
	protected int exitValue;
	protected boolean captureOutput = true;
	protected StreamProxy streamProxy;
	protected ProcessMonitorThread processMonitor;

	/**
	 * 
	 * @param launch
	 * @param process
	 * @param label
	 * @param attributes
	 */
	public ProcessHandlerImpl(LaunchHandler launch, Process process, String label, Map<String, String> attributes) {
		this.launch = launch;
		this.process = process;
		this.label = label;
		this.attributes = attributes;

		this.terminated = true;
		try {
			this.exitValue = process.exitValue();
		} catch (IllegalThreadStateException e) {
			this.terminated = false;
		}

		// String captureOutput = launch.getAttribute(DebugPlugin.ATTR_CAPTURE_OUTPUT);
		// this.captureOutput = !("false".equals(captureOutput)); //$NON-NLS-1$
		streamProxy = createStreamProxy();
		processMonitor = new ProcessMonitorThread(this);
		processMonitor.start();

		launch.addProcess(this);
	}

	protected StreamProxy createStreamProxy() {
		if (!captureOutput) {
			return new NullStreamProxyImpl(getSystemProcess());
		} else {
			// String encoding = getLaunch().getAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING);
			return new StreamProxyImpl(getSystemProcess(), "utf-8");
		}
	}

	@Override
	public Process getSystemProcess() {
		return this.process;
	}

	@Override
	public LaunchHandler getLaunch() {
		return this.launch;
	}

	@Override
	public StreamProxy getStreamProxy() {
		return null;
	}

	@Override
	public boolean isTerminated() {
		return this.terminated;
	}

	@Override
	public boolean canTerminate() {
		return !terminated;
	}

	@Override
	public void terminate() throws IOException {
		if (!isTerminated()) {
			if (this.streamProxy instanceof StreamProxyImpl) {
				((StreamProxyImpl) this.streamProxy).kill();
			}

			Process process = getSystemProcess();
			if (process != null) {
				process.destroy();
			}

			int attempts = 0;
			while (attempts < MAX_WAIT_FOR_DEATH_ATTEMPTS) {
				try {
					process = getSystemProcess();
					if (process != null) {
						exitValue = process.exitValue(); // throws exception if process not exited
					}
					return;
				} catch (IllegalThreadStateException ie) {
				}
				try {
					Thread.sleep(TIME_TO_WAIT_FOR_THREAD_DEATH);
				} catch (InterruptedException e) {
				}
				attempts++;
			}

			throw new IOException("Runtime process terminate failed.");
		}
	}

	class ProcessMonitorThread extends Thread {
		protected boolean fExit;
		protected Process fOSProcess;
		protected ProcessHandler fRuntimeProcess;
		protected Thread fThread;

		/**
		 * A lock protecting access to <code>fThread</code>.
		 */
		private final Object fThreadLock = new Object();

		/**
		 * @see Thread#run()
		 */
		@Override
		public void run() {
			synchronized (fThreadLock) {
				if (fExit) {
					return;
				}
				fThread = Thread.currentThread();
			}
			while (fOSProcess != null) {
				try {
					fOSProcess.waitFor();
				} catch (InterruptedException ie) {
					// clear interrupted state
					Thread.interrupted();
				} finally {
					fOSProcess = null;
					fRuntimeProcess.isTerminated();
				}
			}
			fThread = null;
		}

		/**
		 * Creates a new process monitor and starts monitoring the process for termination.
		 *
		 * @param process
		 *            process to monitor for termination
		 */
		public ProcessMonitorThread(ProcessHandler process) {
			super("Process monitor");
			setDaemon(true);
			fRuntimeProcess = process;
			fOSProcess = process.getSystemProcess();
		}

		protected void killThread() {
			synchronized (fThreadLock) {
				if (fThread == null) {
					fExit = true;
				} else {
					fThread.interrupt();
				}
			}
		}
	}

}
