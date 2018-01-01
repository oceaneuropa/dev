package org.spirit.world.runtime;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.spirit.world.api.World;
import org.spirit.world.api.WorldFactory;

public class WorldFactoryImpl implements WorldFactory {

	protected ServiceRegistration<?> serviceRegistration;

	public void start(BundleContext bundleContext) {
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceRegistration = bundleContext.registerService(WorldFactory.class, this, props);
	}

	public void stop(BundleContext bundleContext) {
		if (this.serviceRegistration != null) {
			this.serviceRegistration.unregister();
			this.serviceRegistration = null;
		}
	}

	@Override
	public World create(String name) {
		return new WorldImpl(name);
	}

}
