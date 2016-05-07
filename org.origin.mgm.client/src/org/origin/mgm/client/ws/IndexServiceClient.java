package org.origin.mgm.client.ws;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.mgm.model.dto.IndexItemDTO;

public class IndexServiceClient extends AbstractClient {

	/**
	 * 
	 * @param config
	 */
	public IndexServiceClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws ClientException
	 */
	public List<IndexItemDTO> getIndexItems(String type) throws ClientException {
		List<IndexItemDTO> indexItemDTOs = null;
		try {
			WebTarget target = getRootPath().path("machines");
			if (type != null) {
				target.queryParam("type", type);
			}
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			Response response = updateHeaders(builder).get();
			checkResponse(response);

			indexItemDTOs = response.readEntity(new GenericType<List<IndexItemDTO>>() {
			});
		} catch (ClientException e) {
			handleException(e);
		}
		if (indexItemDTOs == null) {
			indexItemDTOs = Collections.emptyList();
		}
		return indexItemDTOs;
	}

}
