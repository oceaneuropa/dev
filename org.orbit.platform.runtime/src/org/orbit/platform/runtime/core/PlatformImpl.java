/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.platform.runtime.core;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.platform.runtime.PlatformConstants;
import org.orbit.platform.runtime.command.service.CommandService;
import org.orbit.platform.runtime.command.service.CommandServiceImpl;
import org.orbit.platform.runtime.processes.ProcessManager;
import org.orbit.platform.runtime.processes.ProcessManagerImpl;
import org.orbit.platform.runtime.programs.ProgramsAndFeatures;
import org.orbit.platform.runtime.programs.ProgramsAndFeaturesImpl;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.IProcessManager;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.client.WSClientFactoryImpl;
import org.origin.common.rest.editpolicy.WSEditPolicies;
import org.origin.common.rest.editpolicy.WSEditPoliciesImpl;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see https://en.wikipedia.org/wiki/List_of_nearest_stars_and_brown_dwarfs
 */
public class PlatformImpl implements Platform, IPlatform, IAdaptable {

	protected static Logger LOG = LoggerFactory.getLogger(PlatformImpl.class);

	protected String realm;
	protected ProcessManagerImpl processManager;
	protected WSEditPolicies wsEditPolicies;

	protected BundleContext bundleContext;
	protected CommandService commandService;
	protected ProgramsAndFeatures programsAndFreatures;
	protected WSClientFactory wsClientFactory = new WSClientFactoryImpl();
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected ServiceRegistration<?> serviceRegistry;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	protected RUNTIME_STATE runtimeState = RUNTIME_STATE.STOPPED;

	public PlatformImpl() {
		this.wsEditPolicies = new WSEditPoliciesImpl();
		this.wsEditPolicies.setService(Platform.class, this);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) throws Exception {
		try {
			this.bundleContext = bundleContext;

			// 1. load properties
			Map<Object, Object> properties = new Hashtable<Object, Object>();
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_REALM);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.ORBIT_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_ID);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_NAME);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_VERSION);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_TYPE);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_PARENT_ID);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_HOME);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_HOST_URL);
			PropertyUtil.loadProperty(bundleContext, properties, PlatformConstants.PLATFORM_CONTEXT_ROOT);
			updateProperties(properties);

			// 2. Start managing processes
			this.processManager = new ProcessManagerImpl(this);
			this.processManager.start(bundleContext);

			// 4. Start command service
			this.commandService = new CommandServiceImpl();
			this.commandService.start();

			// 5. Start programs and features service
			this.programsAndFreatures = new ProgramsAndFeaturesImpl(bundleContext);
			this.programsAndFreatures.start();

			// 6. Register as Platform service
			Hashtable<String, Object> props = new Hashtable<String, Object>();
			this.serviceRegistry = bundleContext.registerService(Platform.class, this, props);

			setRuntimeState(RUNTIME_STATE.STARTED);

		} catch (Exception e) {
			setRuntimeState(RUNTIME_STATE.START_FAILED);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		try {
			// Stop Platform service
			if (this.serviceRegistry != null) {
				this.serviceRegistry.unregister();
				this.serviceRegistry = null;
			}

			// 1. Stop programs and features service
			if (this.programsAndFreatures != null) {
				this.programsAndFreatures.stop();
				this.programsAndFreatures = null;
			}

			// 2. Stop command service
			if (this.commandService != null) {
				this.commandService.stop();
			}

			if (this.processManager != null) {
				this.processManager.stop(bundleContext);
				this.processManager = null;
			}

			this.bundleContext = null;

			setRuntimeState(RUNTIME_STATE.STOPPED);

		} catch (Exception e) {
			setRuntimeState(RUNTIME_STATE.STOP_FAILED);
			e.printStackTrace();
		}
	}

	public RUNTIME_STATE getRuntimeState() {
		return this.runtimeState;
	}

	public void setRuntimeState(RUNTIME_STATE runtimeState) {
		this.runtimeState = runtimeState;
	}

	@Override
	public synchronized void updateProperties(Map<Object, Object> properties) {
		LOG.info("updateProperties()");
		if (properties == null) {
			properties = new HashMap<Object, Object>();
		}
		String realm = (String) this.properties.get(PlatformConstants.ORBIT_REALM);
		if (realm != null) {
			this.realm = realm;
		}
		this.properties = properties;
	}

	protected String getProperty(String key) {
		return PropertyUtil.getProperty(this.properties, key, String.class);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getProperty(String key, Class<T> valueClass) {
		// Config properties from bundle context or from system/env properties takes precedence over properties defined in config.ini file.
		Object object = this.properties.get(key);
		if (object != null && valueClass.isAssignableFrom(object.getClass())) {
			return (T) object;
		}
		// If config properties cannot be found, read from config.ini file
		if (this.properties != null && String.class.equals(valueClass)) {
			String value = (String) this.properties.get(key.toString());
			if (value != null) {
				return (T) value;
			}
		}
		return null;
	}

	@Override
	public String getId() {
		String id = (String) this.properties.get(PlatformConstants.PLATFORM_ID);
		return id;
	}

	@Override
	public String getName() {
		String name = (String) this.properties.get(PlatformConstants.PLATFORM_NAME);
		return name;
	}

	@Override
	public String getVersion() {
		String version = (String) this.properties.get(PlatformConstants.PLATFORM_VERSION);
		return version;
	}

	@Override
	public String getParentId() {
		String parentId = (String) this.properties.get(PlatformConstants.PLATFORM_PARENT_ID);
		return parentId;
	}

	@Override
	public String getType() {
		String type = (String) this.properties.get(PlatformConstants.PLATFORM_TYPE);
		return type;
	}

	@Override
	public String getHostURL() {
		String hostURL = getProperty(PlatformConstants.PLATFORM_HOST_URL);
		if (hostURL != null) {
			return hostURL;
		}
		String globalHostURL = getProperty(PlatformConstants.ORBIT_HOST_URL);
		if (globalHostURL != null) {
			return globalHostURL;
		}
		return null;
	}

	@Override
	public String getContextRoot() {
		String contextRoot = getProperty(PlatformConstants.PLATFORM_CONTEXT_ROOT);
		return contextRoot;
	}

	@Override
	public String getHome() {
		String home = getProperty(PlatformConstants.PLATFORM_HOME);
		return home;
	}

	@Override
	public WSEditPolicies getEditPolicies() {
		return this.wsEditPolicies;
	}

	@Override
	public CommandService getCommandService() {
		return this.commandService;
	}

	@Override
	public ProgramsAndFeatures getProgramsAndFeatures() {
		return this.programsAndFreatures;
	}

	@Override
	public ProcessManager getProcessManager() {
		return this.processManager;
	}

	/** Implements IPlatform SDK interface */
	@Override
	public IProcessManager getIProcessManager() {
		return this.processManager;
	}

	/** Implements IAdaptable interface */
	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}

// public static final String PROP_PLATFORM_NAME = "platform.name";
// public static final String PROP_PLATFORM_VERSION = "platform.version";

// public static final String PLATFORM__NAME = "Sun";
// public static final String PLATFORM__VERSION = "1.0.0";

// String getRealm();

// @Override
// public String getRealm() {
// return checkRealm(this.realm);
// }

// protected String checkRealm(String realm) {
// if (realm == null) {
// realm = "default";
// }
// return realm;
// }

// 3. Start command and services
// this.platformCommand = new PlatformCommand();
// this.platformCommand.start(bundleContext);
// this.platformCommand.startGAIA();

// 2. Stop command and services
// if (this.platformCommand != null) {
// this.OSCommand.stopGAIA();
// this.platformCommand.stop(bundleContext);
// this.platformCommand = null;
// }

// @Override
// public PlatformCommand getOSCommand() {
// return this.platformCommand;
// }

// protected IProcess createProcess(String extensionTypeId, String extensionId, String name) {
// return this.processesHandler.createProcess(extensionTypeId, extensionId, name);
// }

// @Override
// public void stop(IProcess process) {
// if (process == null) {
// return;
// }
//
// IPlatformContext context = process.getAdapter(IPlatformContext.class);
// IProgramExtension extension = process.getAdapter(IProgramExtension.class);
//
// if (extension != null) {
// ServiceActivator serviceControl = extension.getAdapter(ServiceActivator.class);
//
// if (serviceControl != null) {
// serviceControl.stop(context, process);
// this.processesService.removeProcess(process);
// }
// }
// }

// public boolean canStart() {
// return false;
// }
//
// public boolean canStop() {
// return false;
// }
//
// public void start() {
//
// }
//
// public void stop() {
//
// }

/// **
// *
// * @param fromState
// * @param toState
// * @return
// */
// protected boolean canChangeState(RUNTIME_STATE fromState, RUNTIME_STATE toState) {
// if (fromState == null) {
// throw new RuntimeException("fromState is null");
// }
// if (toState == null) {
// throw new RuntimeException("toState is null");
// }
//
// if (RUNTIME_STATE.STOPPED.equals(fromState)) {
// // Stopped -> Started
// // Stopped -> StartFailed
// if (RUNTIME_STATE.STARTED.equals(toState) || RUNTIME_STATE.START_FAILED.equals(toState)) {
// return true;
// }
//
// } else if (RUNTIME_STATE.STOP_FAILED.equals(fromState)) {
// // StopFailed -> Started
// // StopFailed -> StartFailed
// // StopFailed -> Stopped
// if (RUNTIME_STATE.STARTED.equals(toState) || RUNTIME_STATE.START_FAILED.equals(toState) || RUNTIME_STATE.STOPPED.equals(toState)) {
// return true;
// }
//
// } else if (RUNTIME_STATE.STARTED.equals(fromState)) {
// // Started -> Stopped
// // Started -> StopFailed
// if (RUNTIME_STATE.STOPPED.equals(toState) || RUNTIME_STATE.STOP_FAILED.equals(toState)) {
// return true;
// }
//
// } else if (RUNTIME_STATE.START_FAILED.equals(fromState)) {
// // StartFailed -> Stopped
// // StartFailed -> StopFailed
// // StartFailed -> Started
// if (RUNTIME_STATE.STOPPED.equals(toState) || RUNTIME_STATE.STOP_FAILED.equals(toState) || RUNTIME_STATE.STARTED.equals(toState)) {
// return true;
// }
// }
//
// return false;
// }
