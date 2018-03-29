package org.origin.common.extensions.util;

import org.origin.common.extensions.core.IExtension;

public interface ExtensionListener {

	void extensionAdded(IExtension extension);

	void extensionRemoved(IExtension extension);

}
