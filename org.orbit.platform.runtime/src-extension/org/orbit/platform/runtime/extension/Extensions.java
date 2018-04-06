package org.orbit.platform.runtime.extension;

import org.orbit.platform.runtime.extension.resources.PlatformNodePreConfigurator;
import org.orbit.platform.runtime.extension.resources.PlatformNodePreConfiguratorPropertyTester;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.origin.common.resources.extension.ResourceConfigurator;

public class Extensions extends ProgramExtensions {

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.platform.runtime");
	}

	@Override
	public void createExtensions() {
		createFolderConfiguratorExtensions();
		createPropertyTesterExtensions();
	}

	protected void createFolderConfiguratorExtensions() {
		String typeId = ResourceConfigurator.TYPE_ID;

		Extension extension = new Extension(typeId, PlatformNodePreConfigurator.ID, "Platform Node Configurator");
		InterfaceDescription desc = new InterfaceDescription(ResourceConfigurator.class, PlatformNodePreConfigurator.class);
		desc.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(PlatformNodePreConfiguratorPropertyTester.ID));
		extension.addInterface(desc);
		addExtension(extension);
	}

	protected void createPropertyTesterExtensions() {
		String typeId = IPropertyTester.TYPE_ID;

		Extension extension = new Extension(typeId, PlatformNodePreConfiguratorPropertyTester.ID);
		extension.addInterface(IPropertyTester.class, PlatformNodePreConfiguratorPropertyTester.class);
		addExtension(extension);
	}

}
