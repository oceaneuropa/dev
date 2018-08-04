package org.orbit.component.connector.tier2.appstore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.orbit.component.api.OrbitClients;
import org.orbit.component.api.OrbitConstants;
import org.orbit.component.api.tier2.appstore.AppStoreClient;
import org.orbit.component.connector.Activator;
import org.orbit.platform.sdk.downloader.Downloader;
import org.origin.common.rest.client.ClientException;

public class AppStoreDownloader implements Downloader {

	public static final String ID = "org.orbit.component.appstore.AppDownloader";

	@Override
	public boolean download(Map<String, Object> args, OutputStream... output) throws IOException {
		String appStoreUrl = Activator.getInstance().getProperty(OrbitConstants.ORBIT_APP_STORE_URL);

		String appId = (String) args.get("appId");
		String appVersion = (String) args.get("appVersion");

		boolean succeed = false;
		boolean hasSucceed = false;
		boolean hasFailed = false;

		AppStoreClient appStore = OrbitClients.getInstance().getAppStore(appStoreUrl);
		if (appStore != null) {
			for (OutputStream currOutput : output) {
				try {
					boolean currSucceed = appStore.downloadAppArchive(appId, appVersion, currOutput);
					if (currSucceed) {
						hasSucceed = true;
					} else {
						hasFailed = true;
					}
				} catch (ClientException e) {
					throw new IOException(e);
				}
			}
		}
		if (hasSucceed && !hasFailed) {
			succeed = true;
		}

		return succeed;
	}

}
