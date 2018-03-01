package org.orbit.platform.sdk.other;

import org.origin.common.adapter.AdaptorSupport;

public abstract class AppLauncherImpl implements AppLauncher {

	public static class ContextImpl implements Context {
		protected AdaptorSupport adaptorSupport = new AdaptorSupport();

		@Override
		public <T> void adapt(Class<T> clazz, T object) {
			this.adaptorSupport.adapt(clazz, object);
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			return this.adaptorSupport.getAdapter(adapter);
		}
	}

}
