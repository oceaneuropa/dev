package org.orbit.component.connector.tier1.identity;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.orbit.component.model.tier1.identity.LoginRequestDTO;
import org.orbit.component.model.tier1.identity.LoginResponseDTO;
import org.orbit.component.model.tier1.identity.LogoutRequestDTO;
import org.orbit.component.model.tier1.identity.RegisterRequestDTO;
import org.origin.common.rest.client.AbstractWSClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;
import org.origin.common.rest.util.ResponseUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Identity web service client.
 * 
 * {contextRoot}: /orbit/v1/identity
 * 
 * Identity: 
 * URL (GET):  {scheme}://{host}:{port}/{contextRoot}/exists?type={elementType}&value={elementValue}
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/register (Body parameter: RegisterRequestDTO)
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/login    (Body parameter: LoginRequestDTO)
 * URL (POST): {scheme}://{host}:{port}/{contextRoot}/logout   (Body parameter: LogoutRequestDTO)
 * 
 */
public class IdentityWSClient extends AbstractWSClient {

	/**
	 * 
	 * @param config
	 */
	public IdentityWSClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Check whether an element (specified by type) with a specified value exists.
	 * 
	 * URL (GET): {scheme}://{host}:{port}/{contextRoot}/exists?type={elementType}&value={elementValue}
	 * 
	 * @param elementType
	 * @param elementValue
	 * @return
	 * @throws ClientException
	 */
	public boolean exists(String elementType, String elementValue) throws ClientException {
		Response response = null;
		try {
			WebTarget target = getRootPath().path("exists");
			target = target.queryParam("type", elementType);
			target = target.queryParam("value", elementValue);

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

			ObjectMapper mapper = createObjectMapper(false);
			Map<?, ?> result = mapper.readValue(responseString, Map.class);
			if (result != null && result.containsKey("exists")) { //$NON-NLS-1$
				Object value = result.get("exists"); //$NON-NLS-1$
				if (value instanceof Boolean) {
					return (boolean) value;
				}
			}

		} catch (ClientException | IOException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return false;
	}

	/**
	 * 
	 * @param requestDTO
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO register(RegisterRequestDTO requestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("register"); //$NON-NLS-1$
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<RegisterRequestDTO>(requestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

	/**
	 * 
	 * @param requestDTO
	 * @return
	 * @throws ClientException
	 */
	public LoginResponseDTO login(LoginRequestDTO requestDTO) throws ClientException {
		LoginResponseDTO result = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("login"); //$NON-NLS-1$
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<LoginRequestDTO>(requestDTO) {
			}));
			checkResponse(target, response);

			result = response.readEntity(LoginResponseDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return result;
	}

	/**
	 * 
	 * @param requestDTO
	 * @return
	 * @throws ClientException
	 */
	public StatusDTO logout(LogoutRequestDTO requestDTO) throws ClientException {
		StatusDTO status = null;
		Response response = null;
		try {
			WebTarget target = getRootPath().path("logout"); //$NON-NLS-1$
			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).post(Entity.json(new GenericEntity<LogoutRequestDTO>(requestDTO) {
			}));
			checkResponse(target, response);

			status = response.readEntity(StatusDTO.class);

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}
		return status;
	}

}
