package org.orbit.component.runtime.relay.other;

import org.orbit.component.runtime.relay.OrbitRelays;
import org.orbit.component.runtime.relay.other.extensions.AppStoreRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.AuthRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.ConfigRegistryRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.DomainServiceRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.MissionControlRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.TransferAgentRelayExtension;
import org.orbit.component.runtime.relay.other.extensions.UserRegistryRelayExtension;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ActivatorV1 implements BundleActivator {

	private static BundleContext context;
	public static BundleContext getContext() {
		return context;
	}

	// tier 1
	protected UserRegistryRelayExtension userRegistryRelayExtension;
	protected AuthRelayExtension authRelayExtension;
	protected ConfigRegistryRelayExtension configRegistryRelayExtension;

	// tier 2
	protected AppStoreRelayExtension appStoreRelayExtension;

	// tier 3
	protected DomainServiceRelayExtension domainServiceRelayExtension;
	protected TransferAgentRelayExtension transferAgentRelayExtension;

	// tier 4
	protected MissionControlRelayExtension missionControlRelayExtension;

	@Override
	public void start(BundleContext context) throws Exception {
		ActivatorV1.context = context;

		// tier 1
		this.userRegistryRelayExtension = new UserRegistryRelayExtension();
		this.userRegistryRelayExtension.register(context);

		this.authRelayExtension = new AuthRelayExtension();
		this.authRelayExtension.register(context);

		this.configRegistryRelayExtension = new ConfigRegistryRelayExtension();
		this.configRegistryRelayExtension.register(context);

		// tier 2
		this.appStoreRelayExtension = new AppStoreRelayExtension();
		this.appStoreRelayExtension.register(context);

		// tier 3
		this.domainServiceRelayExtension = new DomainServiceRelayExtension();
		this.domainServiceRelayExtension.register(context);

		this.transferAgentRelayExtension = new TransferAgentRelayExtension();
		this.transferAgentRelayExtension.register(context);

		// tier 4
		this.missionControlRelayExtension = new MissionControlRelayExtension();
		this.missionControlRelayExtension.register(context);

		OrbitRelays.getInstance().start(context);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		OrbitRelays.getInstance().stop(bundleContext);

		// tier 4
		if (this.missionControlRelayExtension != null) {
			this.missionControlRelayExtension.unregister(bundleContext);
			this.missionControlRelayExtension = null;
		}

		// tier 3
		if (this.domainServiceRelayExtension != null) {
			this.domainServiceRelayExtension.unregister(bundleContext);
			this.domainServiceRelayExtension = null;
		}

		if (this.transferAgentRelayExtension != null) {
			this.transferAgentRelayExtension.unregister(bundleContext);
			this.transferAgentRelayExtension = null;
		}

		// tier 2
		if (this.appStoreRelayExtension != null) {
			this.appStoreRelayExtension.unregister(bundleContext);
			this.appStoreRelayExtension = null;
		}

		// tier 1
		if (this.userRegistryRelayExtension != null) {
			this.userRegistryRelayExtension.unregister(bundleContext);
			this.userRegistryRelayExtension = null;
		}

		if (this.authRelayExtension != null) {
			this.authRelayExtension.unregister(bundleContext);
			this.authRelayExtension = null;
		}

		if (this.configRegistryRelayExtension != null) {
			this.configRegistryRelayExtension.unregister(bundleContext);
			this.configRegistryRelayExtension = null;
		}

		ActivatorV1.context = null;
	}

}

// ProgramExtensionRegistry.getInstance().register(bundleContext, this.authRelayExtension);
// ProgramExtensionRegistry.getInstance().unregister(this.authRelayExtension);
