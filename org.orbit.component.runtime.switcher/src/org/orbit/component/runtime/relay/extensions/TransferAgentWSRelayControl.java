package org.orbit.component.runtime.relay.extensions;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.component.runtime.relay.tier3.TransferAgentWSApplicationDesc;
import org.orbit.component.runtime.relay.util.SwitcherUtil;
import org.orbit.os.runtime.api.WSRelayControl;
import org.origin.common.rest.client.WSClientFactory;
import org.origin.common.rest.server.WSApplicationDescriptiveRelay;
import org.origin.common.rest.switcher.Switcher;
import org.origin.common.rest.switcher.SwitcherPolicy;
import org.osgi.framework.BundleContext;

public class TransferAgentWSRelayControl implements WSRelayControl {

	public static TransferAgentWSRelayControl INSTANCE = new TransferAgentWSRelayControl();

	protected Map<String, WSApplicationDescriptiveRelay> wsAppMap = new HashMap<String, WSApplicationDescriptiveRelay>();

	@Override
	public synchronized void start(BundleContext bundleContext, WSClientFactory factory, String contextRoot, List<URI> uriList) {
		WSApplicationDescriptiveRelay wsApp = this.wsAppMap.get(contextRoot);
		if (wsApp != null) {
			return;
		}

		TransferAgentWSApplicationDesc wsAppDesc = new TransferAgentWSApplicationDesc(contextRoot);
		Switcher<URI> switcher = SwitcherUtil.INSTANCE.createURISwitcher(uriList, SwitcherPolicy.MODE_ROUND_ROBIN);
		WSApplicationDescriptiveRelay newWsApp = new WSApplicationDescriptiveRelay(wsAppDesc, switcher, factory);
		newWsApp.start(bundleContext);

		this.wsAppMap.put(contextRoot, newWsApp);
	}

	@Override
	public synchronized void stop(BundleContext bundleContext, String contextRoot) {
		WSApplicationDescriptiveRelay wsApp = this.wsAppMap.remove(contextRoot);
		if (wsApp != null) {
			wsApp.stop(bundleContext);
		}
	}

}
