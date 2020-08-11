package org.orbit.component.api.util;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.api.ComponentConstants;
import org.orbit.platform.model.program.ProgramManifest;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.WSClientConstants;
import org.origin.common.rest.util.ResponseUtil;

public class WebConsoleClient extends WSClient {

	/**
	 * 
	 * @return
	 */
	public static WebConsoleClient newInstance() {
		String webConsoleUrl = ComponentsConfigPropertiesHandler.getInstance().getProperty(ComponentConstants.ORBIT_WEB_CONSOLE_URL);
		return newInstance(webConsoleUrl);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static WebConsoleClient newInstance(String url) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(WSClientConstants.URL, url);
		WSClientConfiguration config = WSClientConfiguration.create(properties);
		WebConsoleClient client = new WebConsoleClient(config);
		return client;
	}

	/**
	 * 
	 * @param config
	 */
	public WebConsoleClient(WSClientConfiguration config) {
		super(config);
	}

	/**
	 * 
	 * @param appId
	 * @param appVersion
	 * @return
	 * @throws ClientException
	 */
	public ProgramManifest getProgramManifest(String appId, String appVersion) throws ClientException {
		ProgramManifest programManifest = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("/orbit/webconsole/public/apps/" + appId + "_" + appVersion + "/manifest.json");

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);
			if (responseString != null && !responseString.isEmpty()) {
				try {
					// ObjectMapper mapper = createObjectMapper(false);
					// Map<?, ?> responseMap = mapper.readValue(responseString, Map.class);
					// if (responseMap != null) {
					// programManifest = ProgramManifestClientConverter.convert(responseMap);
					// }
					programManifest = ProgramManifestClientConverter.convert(responseString);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return programManifest;
	}

}
