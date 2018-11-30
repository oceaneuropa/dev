package other.orbit.component.runtime.tier1.session.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.component.runtime.ComponentConstants;
import org.orbit.component.runtime.tier1.session.service.OAuth2Service;
import org.orbit.infra.api.InfraConstants;
import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProviderClient;
import org.origin.common.thread.other.ServiceIndexTimerImplV1;
import org.origin.common.thread.other.ServiceIndexTimerV1;

public class OAuth2ServiceIndexTimerV1 extends ServiceIndexTimerImplV1<IndexProviderClient, OAuth2Service> implements ServiceIndexTimerV1<IndexProviderClient, OAuth2Service> {

	public OAuth2ServiceIndexTimerV1(IndexProviderClient indexProvider) {
		super("OAuth2 Service Index Timer", indexProvider);
	}

	@Override
	public OAuth2Service getService() {
		// return Activator.getOAuth2Service();
		return null;
	}

	@Override
	public void updateIndex(IndexProviderClient indexProvider, OAuth2Service service) throws IOException {
		String name = service.getName();
		String hostURL = service.getHostURL();
		String contextRoot = service.getContextRoot();

		IndexItem indexItem = indexProvider.getIndexItem(ComponentConstants.OAUTH2_INDEXER_ID, ComponentConstants.OAUTH2_TYPE, name);
		if (indexItem == null) {
			// Create new index item with properties
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__NAME, name);
			props.put(InfraConstants.SERVICE__HOST_URL, hostURL);
			props.put(InfraConstants.SERVICE__CONTEXT_ROOT, contextRoot);
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.addIndexItem(ComponentConstants.OAUTH2_INDEXER_ID, ComponentConstants.OAUTH2_TYPE, name, props);

		} else {
			// Update existing index item with properties
			Integer indexItemId = indexItem.getIndexItemId();
			Map<String, Object> props = new Hashtable<String, Object>();
			props.put(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME, new Date().getTime());

			indexProvider.setProperties(ComponentConstants.OAUTH2_INDEXER_ID, indexItemId, props);
		}
	}

}
