package org.origin.common.extensions.util;

import org.origin.common.extensions.core.IExtension;
import org.osgi.framework.ServiceReference;

public interface ExtensionFilter {

	boolean accept(ServiceReference<IExtension> reference);

}
