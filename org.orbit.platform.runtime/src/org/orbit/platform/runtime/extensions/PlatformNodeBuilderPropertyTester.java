package org.orbit.platform.runtime.extensions;

import java.io.IOException;
import java.util.Map;

import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.resources.FolderDescription;
import org.origin.common.resources.node.INode;

public class PlatformNodeBuilderPropertyTester implements IPropertyTester {

	public static String ID = "org.orbit.platform.runtime.PlatformNodeBuilderPropertyTester";

	public static PlatformNodeBuilderPropertyTester INSTANCE = new PlatformNodeBuilderPropertyTester();

	@Override
	public boolean accept(Object context, Object source, Object target, Map<String, Object> args) {
		if ((source instanceof INode) && (args != null)) {
			INode node = (INode) source;

			boolean isPlatformNode = false;
			FolderDescription desc = null;
			try {
				desc = node.getDescription();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (desc != null) {
				String typeId = desc.getStringAttribute("typeId");
				if ("platform".equalsIgnoreCase(typeId)) {
					isPlatformNode = true;
				}
			}

			boolean isPreConfig = false;
			String action = (String) args.get("action");
			if ("preconfig".equalsIgnoreCase(action)) {
				isPreConfig = true;
			}

			if (isPlatformNode && isPreConfig) {
				return true;
			}
		}
		return false;
	}

}
