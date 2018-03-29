package org.origin.common.extensions.util;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.extensions.core.IExtension;

public class ExtensionListenerSupport {

	protected List<ExtensionListener> listeners = new ArrayList<ExtensionListener>();

	public void addListener(final ExtensionListener listener) {
		if (listener != null && !this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	public void removeListener(final ExtensionListener listener) {
		if (listener != null && this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		}
	}

	public void notifyExtensionAdded(final IExtension extension) {
		for (ExtensionListener listener : this.listeners) {
			try {
				listener.extensionAdded(extension);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void notifyExtensionRemoved(final IExtension extension) {
		for (ExtensionListener listener : this.listeners) {
			try {
				listener.extensionRemoved(extension);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
