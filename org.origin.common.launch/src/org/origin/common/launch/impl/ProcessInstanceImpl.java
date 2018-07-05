package org.origin.common.launch.impl;

import java.io.IOException;
import java.util.Map;

import org.origin.common.launch.LaunchInstance;
import org.origin.common.launch.ProcessInstance;
import org.origin.common.launch.stream.NullStreamProxyImpl;
import org.origin.common.launch.stream.StreamProxy;
import org.origin.common.launch.stream.StreamProxyImpl;

public class ProcessInstanceImpl implements ProcessInstance {

	private static final int MAX_WAIT_FOR_DEATH_ATTEMPTS = 10;
	private static final int TIME_TO_WAIT_FOR_THREAD_DEATH = 500; // ms

	protected LaunchInstance launchInstance;
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
	 * @param launchInstance
	 * @param process
	 * @param label
	 * @param attributes
	 */
	public ProcessInstanceImpl(LaunchInstance launchInstance, Process process, String label, Map<String, String> attributes) {
		this.launchInstance = launchInstance;
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
		this.streamProxy = createStreamProxy();
		this.processMonitor = new ProcessMonitorThread(this);
		this.processMonitor.start();

		launchInstance.addProcessInstance(this);
	}

	protected StreamProxy createStreamProxy() {
		if (!this.captureOutput) {
			return new NullStreamProxyImpl(getSystemProcess());
		} else {
			// String encoding = getLaunch().getAttribute(DebugPlugin.ATTR_CONSOLE_ENCODING);
			return new StreamProxyImpl(getSystemProcess(), "utf-8");
		}
	}

	@Override
	public LaunchInstance getLaunchInstsance() {
		return this.launchInstance;
	}

	@Override
	public Process getSystemProcess() {
		return this.process;
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
		return !this.terminated;
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
		protected boolean exit;
		protected Process process;
		protected ProcessInstance processInstance;
		protected Thread thread;

		protected Object threadLock = new Object();

		/**
		 * Creates a new process monitor and starts monitoring the process for termination.
		 *
		 * @param processInstance
		 *            process to monitor for termination
		 */
		public ProcessMonitorThread(ProcessInstance processInstance) {
			super("Process monitor");
			setDaemon(true);

			this.processInstance = processInstance;
			this.process = processInstance.getSystemProcess();
		}

		@Override
		public void run() {
			synchronized (this.threadLock) {
				if (this.exit) {
					return;
				}
				this.thread = Thread.currentThread();
			}

			while (this.process != null) {
				try {
					this.process.waitFor();

				} catch (InterruptedException ie) {
					// clear interrupted state
					Thread.interrupted();
				} finally {
					this.process = null;
					this.processInstance.isTerminated();
				}
			}
			this.thread = null;
		}

		protected void killThread() {
			synchronized (this.threadLock) {
				if (this.thread == null) {
					this.exit = true;
				} else {
					this.thread.interrupt();
				}
			}
		}
	}

}
