/*******************************************************************************
 * Copyright (c) 2017, 2018 OceanEuropa.
 * All rights reserved.
 *
 * Contributors:
 *     OceanEuropa - initial API and implementation
 *******************************************************************************/
package org.orbit.spirit.runtime;

import org.orbit.infra.api.indexes.ServiceIndexTimerFactory;
import org.orbit.platform.sdk.serviceactivator.ServiceActivator;
import org.orbit.spirit.runtime.earth.ws.EarthIndexTimerFactory;
import org.orbit.spirit.runtime.extension.EarthServiceActivator;
import org.orbit.spirit.runtime.extension.EarthServicePropertyTester;
import org.orbit.spirit.runtime.extension.GaiaServiceActivator;
import org.orbit.spirit.runtime.extension.GaiaServicePropertyTester;
import org.orbit.spirit.runtime.gaia.ws.GaiaIndexTimerFactory;
import org.origin.common.extensions.Extension;
import org.origin.common.extensions.InterfaceDescription;
import org.origin.common.extensions.ProgramExtensions;
import org.origin.common.extensions.condition.ConditionFactory;
import org.origin.common.extensions.condition.IPropertyTester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spirit extensions
 * 
 */
public class Extensions extends ProgramExtensions {

	protected static Logger LOG = LoggerFactory.getLogger(Extensions.class);

	public static Extensions INSTANCE = new Extensions();

	public Extensions() {
		setBundleId("org.orbit.spirit.runtime");
	}

	@Override
	public void createExtensions() {
		LOG.debug("createExtensions()");

		createServiceActivatorExtensions();
		createPropertyTesterExtensions();
		createIndexProvideExtensions();
	}

	protected void createServiceActivatorExtensions() {
		String extensionTypeId = ServiceActivator.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, GaiaServiceActivator.ID, "GAIA Service Activator");
		InterfaceDescription desc1 = new InterfaceDescription(ServiceActivator.class, GaiaServiceActivator.class);
		desc1.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(GaiaServicePropertyTester.ID));
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, EarthServiceActivator.ID, "Earth Service Activator");
		InterfaceDescription desc2 = new InterfaceDescription(ServiceActivator.class, EarthServiceActivator.class);
		desc2.setTriggerCondition(ConditionFactory.getInstance().newPropertyTesterCondition(EarthServicePropertyTester.ID));
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createPropertyTesterExtensions() {
		String extensionTypeId = IPropertyTester.EXTENSION_TYPE_ID;

		Extension extension1 = new Extension(extensionTypeId, GaiaServicePropertyTester.ID);
		InterfaceDescription desc1 = new InterfaceDescription(IPropertyTester.class, GaiaServicePropertyTester.class);
		extension1.addInterface(desc1);
		addExtension(extension1);

		Extension extension2 = new Extension(extensionTypeId, EarthServicePropertyTester.ID);
		InterfaceDescription desc2 = new InterfaceDescription(IPropertyTester.class, EarthServicePropertyTester.class);
		extension2.addInterface(desc2);
		addExtension(extension2);
	}

	protected void createIndexProvideExtensions() {
		String extensionTypeId = ServiceIndexTimerFactory.EXTENSION_TYPE_ID;
		Class<?> factoryClass = ServiceIndexTimerFactory.class;

		Extension extension11 = new Extension(extensionTypeId, SpiritConstants.IDX__GAIA__INDEXER_ID, "GAIA Index Provider");
		extension11.addInterface(factoryClass, GaiaIndexTimerFactory.class);
		addExtension(extension11);

		Extension extension12 = new Extension(extensionTypeId, SpiritConstants.IDX__EARTH__INDEXER_ID, "Earth Index Provider");
		extension12.addInterface(factoryClass, EarthIndexTimerFactory.class);
		addExtension(extension12);
	}

}

// new Parameter("gaia.host.url", "host url", true), //
// new Parameter("orbit.host.url", "generic host url", true) //
//// GAIA
// Extension gaiaExtension = new Extension(extensionTypeId, GaiaServiceActivatorV0.ID, "GAIA activator", "GAIA activator description");
// InterfaceDescription gaiaDesc = new InterfaceDescription("GAIA", ServiceActivator.class, GaiaServiceActivatorV0.class);
// gaiaDesc.setParameters( //
// new Parameter("gaia.name", "GAIA instance name"), //
// new Parameter("gaia.context_root", "web service context root") //
// );
// gaiaExtension.addInterface(gaiaDesc);
// addExtension(gaiaExtension);
