package org.orbit.infra.api.util;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.extensionregistry.ExtensionItem;
import org.orbit.infra.api.extensionregistry.ExtensionRegistryClient;
import org.orbit.platform.sdk.IPlatform;
import org.orbit.platform.sdk.PlatformSDKActivator;
import org.origin.common.extensions.core.IExtension;
import org.origin.common.extensions.util.ExtensionListener;
import org.origin.common.extensions.util.ExtensionTracker;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtensionsRegister implements ExtensionListener {

	protected static Logger LOG = LoggerFactory.getLogger(ExtensionsRegister.class);

	protected Map<Object, Object> properties;
	protected ExtensionTracker extensionTracker;
	protected ExtensionListener extensionListener;
	protected Timer timer;
	protected TimerTask timerTask;
	protected boolean startListening = false;

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void start(BundleContext bundleContext) throws Exception {
		this.startListening = false;

		this.properties = new Hashtable<Object, Object>();
		PropertyUtil.loadProperty(bundleContext, this.properties, InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);

		if (this.timerTask != null) {
			this.timerTask.cancel();
		}
		this.timerTask = new TimerTask() {
			@Override
			public void run() {
				unregisterExtensions();

				// start tracking all extensions in the platform
				extensionTracker = new ExtensionTracker();
				extensionTracker.addListener(ExtensionsRegister.this);
				extensionTracker.start(bundleContext);

				registerExtensions();
			}
		};
		this.timer = new Timer(true);
		this.timer.schedule(this.timerTask, 5 * 1000);
	}

	/**
	 * 
	 * @param bundleContext
	 * @throws Exception
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		this.startListening = false;

		// stop tracking all extensions in the platform
		if (this.extensionTracker != null) {
			this.extensionTracker.stop(bundleContext);
			this.extensionTracker.removeListener(this);
			this.extensionTracker = null;
		}

		unregisterExtensions();

		if (this.timerTask != null) {
			this.timerTask.cancel();
			this.timerTask = null;
		}
	}

	protected String getPlatformId() {
		String platformId = null;
		if (PlatformSDKActivator.getInstance() != null) {
			IPlatform platform = PlatformSDKActivator.getInstance().getPlatform();
			if (platform != null) {
				platformId = platform.getId();
			}
		}
		return platformId;
	}

	protected String getExtensionRegistryUrl() {
		String url = (String) this.properties.get(InfraConstants.ORBIT_EXTENSION_REGISTRY_URL);
		return url;
	}

	protected ExtensionRegistryClient getExtensionRegistry() {
		String extensionRegistryUrl = getExtensionRegistryUrl();
		ExtensionRegistryClient extensionRegistry = InfraClientsHelper.EXTENSION_REGISTRY.getExtensionRegistryClient(extensionRegistryUrl, null);
		if (extensionRegistry != null && !extensionRegistry.isProxy()) {
			boolean match1 = false;
			try {
				String time1 = String.valueOf(System.currentTimeMillis());
				String time1_echo = extensionRegistry.echo(time1);
				if (time1.equals(time1_echo)) {
					match1 = true;
				}
			} catch (ClientException e) {
				e.printStackTrace();
			}
			boolean match2 = false;
			try {
				String time2 = String.valueOf(System.currentTimeMillis());
				String time2_echo = extensionRegistry.echo(time2);
				if (time2.equals(time2_echo)) {
					match2 = true;
				}
			} catch (ClientException e) {
				e.printStackTrace();
			}
			if (match1 && match2) {
				return extensionRegistry;
			}
		}
		return null;
	}

	protected ExtensionRegistryClient getExtensionRegistry(long timeout) {
		long time1 = System.currentTimeMillis();
		ExtensionRegistryClient extensionRegistry = getExtensionRegistry();
		while (extensionRegistry == null) {
			try {
				Thread.sleep(1 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long time2 = System.currentTimeMillis();
			if (time2 - time1 > timeout) {
				// timeout
				break;
			}
			extensionRegistry = getExtensionRegistry();
		}
		return extensionRegistry;
	}

	protected void registerExtensions() {
		LOG.info("registerExtensions()");
		ExtensionRegistryClient extensionRegistry = getExtensionRegistry(10 * 1000);
		if (extensionRegistry == null) {
			return;
		}

		String[] typeIds = this.extensionTracker.getExtensionTypeIds();

		System.out.println();
		System.out.println("Extensions:");
		System.out.println("------------------------------------------------------------------------");
		int totalSize = 0;
		for (String typeId : typeIds) {
			IExtension[] extensions = this.extensionTracker.getExtensions(typeId);
			System.out.println(typeId + " (" + extensions.length + ")");
			for (IExtension extension : extensions) {
				System.out.println("\t" + extension.getId());
			}
			totalSize += extensions.length;
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("Total number: " + totalSize);
		System.out.println();

		for (String typeId : typeIds) {
			IExtension[] extensions = this.extensionTracker.getExtensions(typeId);
			for (IExtension extension : extensions) {
				try {
					registerExtension(extension);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		this.startListening = true;
	}

	protected void unregisterExtensions() {
		ExtensionRegistryClient extensionRegistry = getExtensionRegistry(10 * 1000);
		if (extensionRegistry == null) {
			return;
		}
		String platformId = getPlatformId();
		try {
			extensionRegistry.removeExtensionItems(platformId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param extension
	 * @throws IOException
	 */
	protected void registerExtension(IExtension extension) throws IOException {
		if (extension == null) {
			return;
		}
		ExtensionRegistryClient extensionRegistry = getExtensionRegistry(10 * 1000);
		if (extensionRegistry == null) {
			return;
		}

		String platformId = getPlatformId();
		String typeId = extension.getTypeId();
		String extensionId = extension.getId();
		String name = extension.getName();
		String description = extension.getDescription();
		Map<Object, Object> props = extension.getProperties();

		Map<String, Object> properties = null;
		if (props != null) {
			properties = new LinkedHashMap<String, Object>();
			for (Iterator<Object> keyItor = props.keySet().iterator(); keyItor.hasNext();) {
				Object propKey = keyItor.next();
				Object propValue = props.get(propKey);
				String propName = propKey.toString();
				properties.put(propName, propValue);
			}
		}

		ExtensionItem extensionItem = extensionRegistry.getExtensionItem(platformId, typeId, extensionId);
		if (extensionItem != null) {
			extensionRegistry.updateExtensionItem(platformId, typeId, extensionId, typeId, extensionId, name, description, properties);
		} else {
			extensionRegistry.addExtensionItem(platformId, typeId, extensionId, name, description, properties);
		}
	}

	/**
	 * 
	 * @param extension
	 * @throws IOException
	 */
	protected void unregisterExtension(IExtension extension) throws IOException {
		if (extension == null) {
			return;
		}
		ExtensionRegistryClient extensionRegistry = getExtensionRegistry(10 * 1000);
		if (extensionRegistry == null) {
			return;
		}
		String platformId = getPlatformId();
		String typeId = extension.getTypeId();
		String extensionId = extension.getId();

		extensionRegistry.removeExtensionItem(platformId, typeId, extensionId);
	}

	@Override
	public void extensionAdded(IExtension extension) {
		if (!this.startListening) {
			return;
		}
		try {
			registerExtension(extension);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void extensionRemoved(IExtension extension) {
		if (!this.startListening) {
			return;
		}
		try {
			unregisterExtension(extension);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
