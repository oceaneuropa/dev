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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.orbit.platform.runtime.core.PlatformContextImpl;
import org.orbit.platform.runtime.core.PlatformImpl;
import org.orbit.platform.runtime.util.ProcessHandlerFilterForProgramExtension;
import org.orbit.platform.runtime.util.ProgramExtensionHelper;
import org.orbit.platform.sdk.IPlatformContext;
import org.orbit.platform.sdk.IProcess;
import org.orbit.platform.sdk.IProcessManager;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.platform.sdk.util.IProcessFilter;
import org.orbit.platform.sdk.util.ServiceActivatorHelper;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.util.ExtensionListener;
import org.origin.common.extensions.util.ExtensionTracker;
import org.osgi.framework.BundleContext;

public class ProcessManagerImpl implements ProcessManager, IProcessManager, ExtensionListener {

	protected PlatformImpl platform;
	protected BundleContext bundleContext;
	protected ExtensionTracker extensionTracker;

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

		// 1. Start thread executor
		this.executor = Executors.newFixedThreadPool(this.numThreads);

		// 2. Start tracking IProgramExtension services
		// - for IProgramExtension service has a ServiceActivator, check whether it is auto start, if so, start the ServiceActivator.
		this.extensionTracker = new ExtensionTracker();
		this.extensionTracker.addListener(this);
		this.extensionTracker.start(bundleContext);
	}

	/**
	 * @see http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
	 * 
	 * @param bundleContext
	 */
	public void stop(BundleContext bundleContext) {
		// 1. Stop tracking IProgramExtension services of ServiceActivator
		// - for IProgramExtension service with ServiceActivator type, stop the ServiceActivator.
		if (this.extensionTracker != null) {
			this.extensionTracker.stop(bundleContext);
			this.extensionTracker.removeListener(this);
			this.extensionTracker = null;
		}

		// 2. Shutdown thread executor
		if (this.executor != null) {
			try {
				System.out.println("attempt to shutdown executor");
				this.executor.shutdown();
				this.executor.awaitTermination(10, TimeUnit.SECONDS);

			} catch (InterruptedException e) {
				System.err.println("tasks interrupted");

			} finally {
				if (!this.executor.isTerminated()) {
					System.err.println("cancel non-finished tasks");
				}
				this.executor.shutdownNow();
				System.out.println("shutdown finished");
			}
			this.executor = null;
		}

		this.bundleContext = null;
	}

	/** implements ProgramExtensionListener */
	@Override
	public void extensionAdded(final IExtension extension) {
		if (extension == null) {
			throw new IllegalArgumentException("extension is null");
		}

		// Autostart process of the extension
		boolean isAutoStart = false;
		final ServiceActivator serviceActivator = extension.getInterface(ServiceActivator.class);
		InterfaceDescription desc = extension.getInterfaceDescription(ServiceActivator.class);

		if (serviceActivator != null && desc != null) {
			IPlatformContext context = createContext(extension);
			isAutoStart = ServiceActivatorHelper.INSTANCE.isServiceActivatorAutoStart(context, desc);

			if (isAutoStart) {
				// boolean sync = false;
				// Callable<ProcessHandler> callable = new Callable<ProcessHandler>() {
				// @Override
				// public ProcessHandler call() throws Exception {
				// return createProcess(extension, serviceActivator, null);
				// }
				// };
				// Future<?> future = this.executor.submit(callable);
				// if (sync) {
				// try {
				// future.get();
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// future.cancel(true);
				//
				// } catch (ExecutionException e) {
				// e.printStackTrace();
				// }
				// }
				try {
					createProcess(extension, serviceActivator, null);
				} catch (ProcessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void extensionRemoved(final IExtension extension) {
		if (extension == null) {
			throw new IllegalArgumentException("extension is null");
		}

		// Stop processes of the extension
		boolean sync = false;
		Future<?> future = this.executor.submit(new Runnable() {
			@Override
			public void run() {
				ProcessHandlerFilterForProgramExtension filter = new ProcessHandlerFilterForProgramExtension(extension);
				ProcessHandler[] processHandlers = getProcessHandlers(filter);
				for (ProcessHandler processHandler : processHandlers) {
					try {
						int pid = processHandler.getProcess().getPID();
						doExitProcess(pid, true);

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
				future.cancel(true);

			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	protected IPlatformContext createContext(IExtension extension) {
		BundleContext bundleContext = extension.getAdapter(BundleContext.class);
		if (bundleContext == null) {
			bundleContext = this.bundleContext;
		}
		PlatformContextImpl context = new PlatformContextImpl(bundleContext, this.platform);
		return context;
	}

	/**
	 * @see org.eclipse.jgit.internal.storage.pack.PackWriter
	 * @see org.eclipse.jgit.util.FS
	 * 
	 * @param extension
	 * @param properties
	 * @throws ProcessException
	 */
	public int createProcess(final IExtension extension, final Map<Object, Object> properties) throws ProcessException {
		ServiceActivator activator = extension.getInterface(ServiceActivator.class);
		if (activator == null) {
			// Do not start process if ServiceActivator is not available.
			throw new ProcessException("ServiceActivator is not available from the extension.");
		}

		ProcessHandler processHandler = null;
		if (ProgramExtensionHelper.INSTANCE.isSingleton(extension, activator)) {
			// Do not start if process already exists for the extension
			ProcessHandlerFilterForProgramExtension filter = new ProcessHandlerFilterForProgramExtension(extension);
			ProcessHandler[] processHandlers = getProcessHandlers(filter);
			if (processHandlers.length > 0) {
				processHandler = processHandlers[0];
			}
		}

		if (processHandler == null) {
			// Create a new process for the extension
			Callable<ProcessHandler> callable = new Callable<ProcessHandler>() {
				@Override
				public ProcessHandler call() throws Exception {
					return createProcess(extension, activator, properties);
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
				if (e.getCause() instanceof ProcessException) {
					throw (ProcessException) e.getCause();
				}
			}
		}

		int pid = -1;
		if (processHandler != null && processHandler.getProcess() != null) {
			pid = processHandler.getProcess().getPID();
		}
		return pid;
	}

	/**
	 * 
	 * @param extension
	 * @param serviceActivator
	 * @param properties
	 * @return
	 * @throws ProcessException
	 */
	public ProcessHandler createProcess(IExtension extension, ServiceActivator serviceActivator, Map<Object, Object> properties) throws ProcessException {
		this.processesLock.writeLock().lock();
		try {
			int pid = getNextPID();
			String name = ProgramExtensionHelper.INSTANCE.getName(extension, serviceActivator);

			IPlatformContext context = createContext(extension);
			context.setProperties(properties);

			ProcessImpl process = new ProcessImpl(pid, name);
			process.adapt(IExtension.class, extension);
			process.adapt(IPlatformContext.class, context);

			ProcessHandlerForServiceActivator processHandler = new ProcessHandlerForServiceActivator(this, extension, context, process);
			processHandler.start();

			this.processHandlers.add(processHandler);

			return processHandler;

		} finally {
			this.processesLock.writeLock().unlock();
		}
	}

	protected int getNextPID() {
		this.processesLock.readLock().lock();
		try {
			int pid = 1;
			for (ProcessHandler currProcessHandler : this.processHandlers) {
				int currPID = currProcessHandler.getProcess().getPID();
				if (pid == currPID) {
					pid += 1;
				}
			}
			return pid;
		} finally {
			this.processesLock.readLock().unlock();
		}
	}

	@Override
	public boolean startProcess(int pid, boolean async) throws ProcessException {
		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return doStartProcess(pid);
			}
		};
		Future<Boolean> future = this.executor.submit(callable);
		if (!async) {
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// e.printStackTrace();
				Throwable throwable = e.getCause();
				if (throwable instanceof ProcessException) {
					throw (ProcessException) throwable;
				}
			}
		}
		return false;
	}

	@Override
	public boolean stopProcess(int pid, boolean async) throws ProcessException {
		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return doStopProcess(pid, true);
			}
		};
		Future<Boolean> future = this.executor.submit(callable);
		if (!async) {
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// e.printStackTrace();
				Throwable throwable = e.getCause();
				if (throwable instanceof ProcessException) {
					throw (ProcessException) throwable;
				}
			}
		}
		return false;
	}

	@Override
	public boolean exitProcess(final int pid, boolean async) throws ProcessException {
		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return doExitProcess(pid, true);
			}
		};
		Future<Boolean> future = this.executor.submit(callable);
		if (!async) {
			try {
				return future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// e.printStackTrace();
				Throwable throwable = e.getCause();
				if (throwable instanceof ProcessException) {
					throw (ProcessException) throwable;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param pid
	 * @return
	 * @throws ProcessException
	 */
	protected boolean doStartProcess(int pid) throws ProcessException {
		this.processesLock.writeLock().lock();
		try {
			ProcessHandler processHandler = getProcessHandler(pid);
			if (processHandler == null) {
				throw new ProcessException("Process '" + pid + "' does not exists.");
			}
			if (processHandler.getRuntimeState().isStarted()) {
				throw new ProcessException("Process '" + pid + "' is already started.");
			}
			if (!processHandler.canStart()) {
				throw new ProcessException("Process '" + pid + "' cannot be started.");
			}
			processHandler.start();
			if (processHandler.getRuntimeState().isStarted()) {
				return true;
			}
			return false;
		} finally {
			this.processesLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param pid
	 * @param force
	 * @return
	 * @throws ProcessException
	 */
	protected boolean doStopProcess(int pid, boolean force) throws ProcessException {
		this.processesLock.writeLock().lock();
		try {
			ProcessHandler processHandler = getProcessHandler(pid);
			if (processHandler == null) {
				throw new ProcessException("Process '" + pid + "' does not exists.");
			}
			if (processHandler.getRuntimeState().isStopped()) {
				throw new ProcessException("Process '" + pid + "' is already stopped.");
			}
			if (!processHandler.canStop()) {
				throw new ProcessException("Process '" + pid + "' cannot be stopped.");
			}
			processHandler.stop();
			if (processHandler.getRuntimeState().isStopped()) {
				return true;
			}
			return false;
		} finally {
			this.processesLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param pid
	 * @param force
	 * @return
	 * @throws ProcessException
	 */
	protected boolean doExitProcess(int pid, boolean force) throws ProcessException {
		this.processesLock.writeLock().lock();
		try {
			ProcessHandler processHandler = getProcessHandler(pid);
			if (processHandler != null) {
				if (((ProcessHandlerForServiceActivator) processHandler).canStop()) {
					((ProcessHandlerForServiceActivator) processHandler).stop();
				}

				boolean doRemoveProcessHandler = false;
				if (processHandler.getRuntimeState().isStopped() || force) {
					doRemoveProcessHandler = true;
				}

				if (doRemoveProcessHandler) {
					return this.processHandlers.remove(processHandler);
				}
			}
			return false;
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
