/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.processes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.orbit.platform.runtime.platform.PlatformContextImpl;
import org.orbit.platform.runtime.platform.PlatformImpl;
import org.orbit.platform.runtime.util.ProcessHandlerFilterForProgramExtension;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessFilter;
import org.orbit.platform.sdk.IProcessManager;
import org.orbit.platform.sdk.ProcessImpl;
import org.orbit.platform.sdk.ServiceActivator;
import org.orbit.platform.sdk.extension.IProgramExtension;
import org.orbit.platform.sdk.extension.util.ProgramExtensionTracker;
import org.orbit.platform.sdk.extension.util.ProgramExtensionTracker.ProgramExtensionListener;
import org.osgi.framework.BundleContext;

public class ProcessManagerImpl implements ProcessManager, IProcessManager, ProgramExtensionListener {

	protected PlatformImpl platform;
	protected BundleContext bundleContext;
	protected ProgramExtensionTracker programExtensionTracker;

	protected int numThreads;
	protected ExecutorService executor;
	protected ReadWriteLock processesLock;

	protected List<ProcessHandler> processHandlers = new ArrayList<ProcessHandler>();

	/**
	 * 
	 * @param platform
	 */
	public ProcessManagerImpl(PlatformImpl platform) {
		this.platform = platform;
		this.numThreads = 5;
		this.processesLock = new ReentrantReadWriteLock();
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void start(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		this.executor = Executors.newFixedThreadPool(this.numThreads);

		// Start tracking IProgramExtension services
		// - for IProgramExtension service has a ServiceActivator, check whether it is auto start, if so, start the ServiceActivator.
		this.programExtensionTracker = new ProgramExtensionTracker();
		this.programExtensionTracker.addListener(this);
		this.programExtensionTracker.start(bundleContext);
	}

	/**
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// Stop tracking IProgramExtension services of ServiceActivator
		// - for IProgramExtension service with ServiceActivator type, stop the ServiceActivator.
		if (this.programExtensionTracker != null) {
			this.programExtensionTracker.stop(bundleContext);
			this.programExtensionTracker.removeListener(this);
			this.programExtensionTracker = null;
		}

		if (this.executor != null) {
			this.executor.shutdown();
			this.executor = null;
		}

		this.bundleContext = null;
	}

	/** implements ProgramExtensionListener */
	@Override
	public void extensionAdded(final IProgramExtension extension) {
		if (extension == null) {
			throw new IllegalArgumentException("extension is null");
		}

		// Autostart process of the extension
		final ServiceActivator serviceActivator = extension.getAdapter(ServiceActivator.class);

		if (serviceActivator != null && serviceActivator.isAutoStart()) {
			boolean sync = false;

			Future<?> future = this.executor.submit(new Runnable() {
				@Override
				public void run() {
					doStartProcess(extension, serviceActivator);
				}
			});
			if (sync) {
				try {
					future.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void extensionRemoved(final IProgramExtension extension) {
		if (extension == null) {
			throw new IllegalArgumentException("extension is null");
		}

		boolean sync = false;

		// Stop processes of the extension
		Future<?> future = this.executor.submit(new Runnable() {
			@Override
			public void run() {
				ProcessHandlerFilterForProgramExtension filter = new ProcessHandlerFilterForProgramExtension(extension);
				ProcessHandler[] processHandlers = getProcessHandlers(filter);
				for (ProcessHandler processHandler : processHandlers) {
					try {
						int pid = processHandler.getProcess().getPID();
						doStopProcess(pid);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		if (sync) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	protected IPlatformContext createContext() {
		PlatformContextImpl context = new PlatformContextImpl();
		context.setBundleContext(this.bundleContext);
		context.setPlatform(this.platform);
		return context;
	}

	/**
	 * @see org.eclipse.jgit.internal.storage.pack.PackWriter
	 * @see org.eclipse.jgit.util.FS
	 * 
	 * @param extension
	 */
	@Override
	public ProcessHandler startProcess(IProgramExtension extension, Map<String, Object> properties) {
		ServiceActivator serviceActivator = extension.getAdapter(ServiceActivator.class);
		if (serviceActivator == null) {
			// Do not start process if ServiceActivator is not available.
			return null;
		}

		ProcessHandler processHandler = null;

		boolean doStart = false;

		if (serviceActivator.isSingleInstance()) {
			// Do not start if IProcess already exists for the IProgramExtension
			ProcessHandlerFilterForProgramExtension filter = new ProcessHandlerFilterForProgramExtension(extension);
			ProcessHandler[] processHandlers = getProcessHandlers(filter);
			if (processHandlers.length > 0) {
				processHandler = processHandlers[0];
			}

		} else {
			// Create a new IProcess for the IProgramExtension
			doStart = true;
		}

		if (doStart) {
			Callable<ProcessHandler> callable = new Callable<ProcessHandler>() {
				@Override
				public ProcessHandler call() throws Exception {
					return doStartProcess(extension, serviceActivator);
				}
			};
			Future<ProcessHandler> future = this.executor.submit(callable);
			try {
				processHandler = future.get();

			} catch (InterruptedException e) {
				e.printStackTrace();
				future.cancel(true);

			} catch (ExecutionException e) {
				e.printStackTrace();
				future.cancel(true);
			}
		}

		return processHandler;
	}

	/**
	 * 
	 * @param extension
	 * @param serviceActivator
	 */
	protected ProcessHandler doStartProcess(IProgramExtension extension, ServiceActivator serviceActivator) {
		this.processesLock.writeLock().lock();
		try {
			int pid = getNextPID();
			String processName = serviceActivator.getProcessName();
			IPlatformContext context = createContext();

			ProcessImpl process = new ProcessImpl(pid, processName);
			process.adapt(IProgramExtension.class, extension);
			process.adapt(IPlatformContext.class, context);

			ProcessHandlerImpl processHandler = new ProcessHandlerImpl(this, extension, context, process);
			processHandler.doStart();

			this.processHandlers.add(processHandler);

			return processHandler;

		} finally {
			this.processesLock.writeLock().unlock();
		}
	}

	protected int getNextPID() {
		int pid = 1;
		for (ProcessHandler currProcessHandler : this.processHandlers) {
			int currPID = currProcessHandler.getProcess().getPID();
			if (pid == currPID) {
				pid += 1;
			}
		}
		return pid;
	}

	@Override
	public void stopProcess(final int pid, boolean sync) {
		Future<?> future = this.executor.submit(new Runnable() {
			@Override
			public void run() {
				doStopProcess(pid);
			}
		});
		if (sync) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param pid
	 */
	protected void doStopProcess(int pid) {
		this.processesLock.writeLock().lock();
		try {
			ProcessHandler processHandler = getProcessHandler(pid);
			if (processHandler != null) {
				((ProcessHandlerImpl) processHandler).doStop();

				if (processHandler.getRuntimeState().isStopped()) {
					this.processHandlers.remove(processHandler);
				}
			}
		} finally {
			this.processesLock.writeLock().unlock();
		}
	}

	@Override
	public ProcessHandler[] getProcessHandlers() {
		this.processesLock.readLock().lock();
		try {
			return this.processHandlers.toArray(new ProcessHandler[this.processHandlers.size()]);
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public ProcessHandler[] getProcessHandlers(ProcessHandlerFilter filter) {
		this.processesLock.readLock().lock();
		try {
			List<ProcessHandler> resultProcessHandlers = new ArrayList<ProcessHandler>();
			for (ProcessHandler currProcessHandler : this.processHandlers) {
				if (filter == null || filter.accept(currProcessHandler)) {
					resultProcessHandlers.add(currProcessHandler);
				}
			}
			return resultProcessHandlers.toArray(new ProcessHandler[resultProcessHandlers.size()]);
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public ProcessHandler getProcessHandler(int pid) {
		this.processesLock.readLock().lock();
		try {
			ProcessHandler processHandler = null;
			for (ProcessHandler currProcessHandler : this.processHandlers) {
				int currPID = currProcessHandler.getProcess().getPID();
				if (pid == currPID) {
					processHandler = currProcessHandler;
					break;
				}
			}
			return processHandler;
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public IProcess[] getProcesses() {
		this.processesLock.readLock().lock();
		try {
			List<IProcess> processes = new ArrayList<IProcess>();
			for (ProcessHandler processHandler : this.processHandlers) {
				IProcess process = processHandler.getProcess();
				processes.add(process);
			}
			return processes.toArray(new IProcess[processes.size()]);
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public IProcess[] getProcesses(IProcessFilter filter) {
		this.processesLock.readLock().lock();
		try {
			List<IProcess> processes = new ArrayList<IProcess>();
			for (ProcessHandler processHandler : this.processHandlers) {
				IProcess process = processHandler.getProcess();
				if (filter == null || filter.accept(process)) {
					processes.add(process);
				}
			}
			return processes.toArray(new IProcess[processes.size()]);
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public IProcess getProcess(int pid) {
		this.processesLock.readLock().lock();
		try {
			IProcess process = null;
			for (ProcessHandler processHandler : this.processHandlers) {
				int currPID = processHandler.getProcess().getPID();
				if (pid == currPID) {
					process = processHandler.getProcess();
				}
			}
			return process;
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

}

// protected void startExtensionServices() {
// IProgramExtensionService extensionService = this.platform.getProgramExtensionService();
// if (extensionService != null) {
// String[] extensionTypeIds = extensionService.getExtensionTypeIds();
// for (String extensionTypeId : extensionTypeIds) {
// IProgramExtension[] extensions = extensionService.getExtensions(extensionTypeId);
// if (extensions != null) {
// for (IProgramExtension extension : extensions) {
// autoStartServiceActivator(extension);
// }
// }
// }
// }
// }

// @Override
// public IProcess createProcess(IProgramExtension extension, String processName) {
// int pid = getNextPID();
//
// ProcessImpl process = new ProcessImpl(pid, processName);
//
// ProcessHandlerImpl processHandler = new ProcessHandlerImpl(this, extension, process);
// this.processHandlers.add(processHandler);
//
// return process;
// }

// @Override
// public boolean removeProcess(IProcess process) {
// if (process != null) {
// int pid = process.getPID();
// ProcessHandler processHandler = getProcessHandler(pid);
// if (processHandler != null) {
// return this.processHandlers.remove(processHandler);
// }
// }
// return false;
// }

// @Override
// public List<IProcess> getProcesses(String extensionTypeId) {
// List<IProcess> processes = new ArrayList<IProcess>();
// for (ProcessHandler processHandler : this.processHandlers) {
// String currExtensionTypeId = processHandler.getExtension().getTypeId();
// if (extensionTypeId != null && extensionTypeId.equals(currExtensionTypeId)) {
// IProcess process = processHandler.getProcess();
// processes.add(process);
// }
// }
// return processes;
// }

// @Override
// public List<IProcess> getProcesses(String extensionTypeId, String extensionId) {
// List<IProcess> processes = new ArrayList<IProcess>();
// for (ProcessHandler processHandler : this.processHandlers) {
// String currExtensionTypeId = processHandler.getExtension().getTypeId();
// String currExtensionId = processHandler.getExtension().getId();
// if (extensionTypeId != null && extensionTypeId.equals(currExtensionTypeId) //
// && extensionId != null && extensionId.equals(currExtensionId)//
// ) {
// IProcess process = processHandler.getProcess();
// processes.add(process);
// }
// }
// return processes;
// }
