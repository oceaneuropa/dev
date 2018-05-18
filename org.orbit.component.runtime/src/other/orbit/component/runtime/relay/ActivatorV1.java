package other.orbit.component.runtime.relay;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import other.orbit.component.runtime.relay.extensions.AppStoreRelayExtension;
import other.orbit.component.runtime.relay.extensions.AuthRelayExtension;
import other.orbit.component.runtime.relay.extensions.ConfigRegistryRelayExtension;
import other.orbit.component.runtime.relay.extensions.DomainManagementRelayExtension;
import other.orbit.component.runtime.relay.extensions.MissionControlRelayExtension;
import other.orbit.component.runtime.relay.extensions.NodeControlRelayExtension;
import other.orbit.component.runtime.relay.extensions.UserRegistryRelayExtension;

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
	protected DomainManagementRelayExtension domainServiceRelayExtension;
	protected NodeControlRelayExtension transferAgentRelayExtension;

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
		this.domainServiceRelayExtension = new DomainManagementRelayExtension();
		this.domainServiceRelayExtension.register(context);

		this.transferAgentRelayExtension = new NodeControlRelayExtension();
		this.transferAgentRelayExtension.register(context);

		// tier 4
		this.missionControlRelayExtension = new MissionControlRelayExtension();
		this.missionControlRelayExtension.register(context);

		// OrbitRelays.getInstance().start(context);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		// OrbitRelays.getInstance().stop(bundleContext);

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
