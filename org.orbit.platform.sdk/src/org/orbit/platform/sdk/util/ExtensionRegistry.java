package org.orbit.platform.sdk.util;

import org.origin.common.extensions.core.IExtension;

public interface ExtensionRegistry {

	IExtension[] getExtensions(String typeId);

	IExtension getExtension(String typeId, String id);

}
