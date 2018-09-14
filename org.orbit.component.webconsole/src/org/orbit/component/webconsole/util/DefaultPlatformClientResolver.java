package org.orbit.component.webconsole.util;

import java.io.IOException;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.util.InfraClientsUtil;
import org.orbit.platform.api.PlatformClient;
import org.orbit.platform.api.PlatformClientResolver;

public class DefaultPlatformClientResolver implements PlatformClientResolver {

	protected String indexServiceUrl;
	protected String accessToken;

	/**
	 * 
	 * @param indexServiceUrl
	 * @param accessToken
	 */
	public DefaultPlatformClientResolver(String indexServiceUrl, String accessToken) {
		this.indexServiceUrl = indexServiceUrl;
		this.accessToken = accessToken;
	}

	@Override
	public PlatformClient resolve(String parentPlatformId, String platformId, String... platformTypes) throws IOException {
		PlatformClient platformClient = null;

		IndexItem platformIndexItem = InfraClientsUtil.Indexes.getIndexItem(this.indexServiceUrl, this.accessToken, parentPlatformId, platformId, platformTypes);
		if (platformIndexItem != null) {
			platformClient = OrbitClientHelper.INSTANCE.getPlatformClient(this.accessToken, platformIndexItem);
		}

		return platformClient;
	}

}
