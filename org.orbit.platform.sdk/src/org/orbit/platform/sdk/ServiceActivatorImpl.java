package org.orbit.platform.sdk;

public abstract class ServiceActivatorImpl implements ServiceActivator {

	@Override
	public boolean isAutoStart() {
		return false;
	}

	@Override
	public boolean isSingleInstance() {
		return true;
	}

}
