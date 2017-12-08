package org.origin.common.rest.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.ErrorDTO;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.model.Responses;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtil {

	@SuppressWarnings("unchecked")
	public static <T> T getSimpleValue(Response response, String attributeName, Class<T> clazz) throws ClientException {
		T t = null;
		try {
			if (response != null) {
				String responseString = response.readEntity(String.class);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Map<?, ?> result = mapper.readValue(responseString, Map.class);
				if (result != null) {
					Object value = result.get(attributeName);
					if (value != null && clazz.isAssignableFrom(value.getClass())) {
						t = (T) value;
					}
				}
			}
		} catch (Exception e) {
			throw new ClientException(500, e.getMessage(), e);
		}
		return t;
	}

	/**
	 * Convert Exception to Error DTO.
	 * 
	 * @param e
	 * @param errorCode
	 * @return
	 */
	public static ErrorDTO toDTO(Exception e, String errorCode) {
		if (e == null) {
			return null;
		}

		ErrorDTO dto = new ErrorDTO();

		dto.setCode(errorCode);
		dto.setMessage(e.getMessage());

		if (e.getCause() != null) {
			String causeName = e.getCause().getClass().getName();
			String causeMessage = e.getCause().getMessage();
			dto.setDetail(causeName + " " + causeMessage);
		} else {
			String causeName = e.getClass().getName();
			dto.setDetail(causeName);
		}

		return dto;
	}

	/**
	 * Check whether a Response is successful or not.
	 * 
	 * @param response
	 * @return Return true if the Response is successful. Otherwise, return false.
	 */
	public static boolean isSuccessful(Response response) {
		if (response != null && response.getStatusInfo() != null) {
			if (Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Close the Closeable objects and <b>ignore</b> any {@link IOException} or null pointers. Must only be used for cleanup in exception handlers.
	 * 
	 * @param response
	 *            Close javax.ws.rs.core.Response.
	 * @param printStackTrace
	 */
	public static void closeQuietly(Response response, boolean printStackTrace) {
		try {
			if (response != null) {
				response.close();
			}
		} catch (Exception ioe) {
			if (printStackTrace) {
				ioe.printStackTrace();
			} else {
				// ignore
			}
		}
	}

	/**
	 * 
	 * @param response
	 * @return
	 */
	public static Responses parseResponses(Response response) {
		Responses responses = null;
		try {
			responses = response.readEntity(Responses.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (responses == null) {
			String responseString = response.readEntity(String.class);
			responses = parseResponses(responseString);
		}
		return responses;
	}

	/**
	 * {"requestName":"add_machine_config","parameters":{"machineId":"m006","name":"machine006","ipAddress":"192.168.0.6"},"machineId":"m006","name":
	 * "machine006","ipAddress":"192.168.0.6"}
	 * 
	 * @param requestString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Request parseRequest(String requestString) {
		Request request = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> result = mapper.readValue(requestString, Map.class);

			if (result != null && result.containsKey("requestName")) {
				String requestName = (String) result.get("requestName");
				Map<String, Object> parameters = null;
				if (result.containsKey("parameters")) {
					parameters = (Map<String, Object>) result.get("parameters");
				}

				if (requestName != null && !requestName.isEmpty()) {
					request = new Request(requestName, parameters);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * 
	 * @param responsesString
	 * @return
	 */
	public static Responses parseResponses(String responsesString) {
		Responses responses = null;

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Map<String, Object> map = mapper.readValue(responsesString, Map.class);
			if (map != null) {
				responses = new Responses();

				// requestName
				String requestName = null;
				Object requestNameObj = map.get("requestName");
				if (requestNameObj != null) {
					requestName = requestNameObj.toString();
				}
				responses.setRequestName(requestName);

				// responseMap
				Object responseMapObj = map.get("responseMap");
				if (responseMapObj instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> responseMap = (Map<String, Object>) responseMapObj;

					for (Iterator<String> keyItor = responseMap.keySet().iterator(); keyItor.hasNext();) {
						String key = keyItor.next();
						Object value = responseMap.get(key);

						org.origin.common.rest.model.Response response = convertToResponse(value);
						if (response != null) {
							responses.setResponse(key, response);
						} else {
							responses.setResponse(key, value);
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return responses;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected static org.origin.common.rest.model.Response convertToResponse(Object value) {
		org.origin.common.rest.model.Response response = null;
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;

			String status = null;
			String message = null;
			Throwable exception = null;

			Object statusObj = map.get("status");
			if (statusObj != null) {
				status = statusObj.toString();
			}

			Object messageObj = map.get("message");
			if (messageObj != null) {
				message = messageObj.toString();
			}

			Object exceptionObj = map.get("exception");
			if (exceptionObj != null) {
				exception = convertToThrowable(exceptionObj);
			}

			Object bodyObj = map.get("body");

			if (status != null && (message != null || exception != null)) {
				response = new org.origin.common.rest.model.Response(status, message, exception);
				response.setBody(bodyObj);
			}
		}
		return response;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	protected static Throwable convertToThrowable(Object value) {
		Throwable throwable = null;
		if (value instanceof Throwable) {
			throwable = (Throwable) value;

		} else if (value instanceof Map) {
			Map<?, ?> causeMap = (Map<?, ?>) value;

			while (causeMap != null) {
				if (causeMap.containsKey("cause") && causeMap.containsKey("stackTrace")) {
					// cause
					String cause = null;
					Object causeObj = causeMap.get("cause");
					if (causeObj != null) {
						cause = causeObj.toString();
					}

					// stackTrace
					List<StackTraceElement> stackTraceElements = new ArrayList<StackTraceElement>();
					Object stackTraceObj = causeMap.get("stackTrace");
					if (stackTraceObj instanceof List) {
						List<?> stackTraceList = (List<?>) stackTraceObj;
						for (Object stackTraceItem : stackTraceList) {
							if (stackTraceItem instanceof Map) {
								Map<?, ?> stackTraceMap = (Map<?, ?>) stackTraceItem;

								String declareClassName = (String) stackTraceMap.get("className");
								String methodName = (String) stackTraceMap.get("methodName");
								String fileName = (String) stackTraceMap.get("fileName");
								int lineNumber = (int) stackTraceMap.get("lineNumber");

								StackTraceElement stackTraceElement = new StackTraceElement(declareClassName, methodName, fileName, lineNumber);
								stackTraceElements.add(stackTraceElement);
							}
						}
					}

					if (causeObj != null || stackTraceObj != null) {
						Exception exception = new Exception(cause);
						exception.setStackTrace(stackTraceElements.toArray(new StackTraceElement[stackTraceElements.size()]));

						throwable = exception;
						break;
					}
				}

				if (causeMap.get("cause") instanceof Map) {
					causeMap = (Map<?, ?>) causeMap.get("cause");
				}
			}
		}
		return throwable;
	}

}

// {cause={
// cause=null,
// stackTrace=[
// {methodName=receiveErrorResponse, fileName=QueryExecutorImpl.java, lineNumber=2284, className=org.postgresql.core.v3.QueryExecutorImpl, nativeMethod=false},
// {methodName=processResults, fileName=QueryExecutorImpl.java, lineNumber=2003, className=org.postgresql.core.v3.QueryExecutorImpl, nativeMethod=false},
// {methodName=execute, fileName=QueryExecutorImpl.java, lineNumber=200, className=org.postgresql.core.v3.QueryExecutorImpl, nativeMethod=false},
// {methodName=execute, fileName=PgStatement.java, lineNumber=424, className=org.postgresql.jdbc.PgStatement, nativeMethod=false},
// {methodName=executeWithFlags, fileName=PgPreparedStatement.java, lineNumber=161, className=org.postgresql.jdbc.PgPreparedStatement, nativeMethod=false},
// {methodName=executeUpdate, fileName=PgPreparedStatement.java, lineNumber=133, className=org.postgresql.jdbc.PgPreparedStatement, nativeMethod=false},
// {methodName=update, fileName=DatabaseUtil.java, lineNumber=413, className=org.origin.common.jdbc.DatabaseUtil, nativeMethod=false},
// {methodName=addMachineConfig, fileName=MachineConfigTableHandler.java, lineNumber=127,
// className=org.orbit.component.server.tier3.domain.handler.MachineConfigTableHandler, nativeMethod=false},
// {methodName=addMachineConfig, fileName=DomainManagementServiceDatabaseImpl.java, lineNumber=273,
// className=org.orbit.component.server.tier3.domain.service.impl.DomainManagementServiceDatabaseImpl, nativeMethod=false},
// {methodName=execute, fileName=AddMachineConfigCommand.java, lineNumber=45, className=org.orbit.component.server.tier3.domain.command.AddMachineConfigCommand,
// nativeMethod=false},
// {methodName=execute, fileName=CommandStack.java, lineNumber=27, className=org.origin.common.command.impl.CommandStack, nativeMethod=false},
// {methodName=onRequest, fileName=AbstractApplicationRequestResponseResource.java, lineNumber=133,
// className=org.origin.common.rest.agent.AbstractApplicationRequestResponseResource, nativeMethod=false},
// {methodName=invoke0, fileName=NativeMethodAccessorImpl.java, lineNumber=-2, className=sun.reflect.NativeMethodAccessorImpl, nativeMethod=true},
// {methodName=invoke, fileName=NativeMethodAccessorImpl.java, lineNumber=62, className=sun.reflect.NativeMethodAccessorImpl, nativeMethod=false},
// {methodName=invoke, fileName=DelegatingMethodAccessorImpl.java, lineNumber=43, className=sun.reflect.DelegatingMethodAccessorImpl, nativeMethod=false},
// {methodName=invoke, fileName=Method.java, lineNumber=483, className=java.lang.reflect.Method, nativeMethod=false},
// {methodName=invoke, fileName=ResourceMethodInvocationHandlerFactory.java, lineNumber=81,
// className=org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory$1, nativeMethod=false},
// {methodName=run, fileName=AbstractJavaResourceMethodDispatcher.java, lineNumber=144,
// className=org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher$1, nativeMethod=false},
// {methodName=invoke, fileName=AbstractJavaResourceMethodDispatcher.java, lineNumber=161,
// className=org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher, nativeMethod=false},
// {methodName=doDispatch, fileName=JavaResourceMethodDispatcherProvider.java, lineNumber=160,
// className=org.glassfish.jersey.server.model.internal.JavaResourceMethodDispatcherProvider$ResponseOutInvoker, nativeMethod=false},
// {methodName=dispatch, fileName=AbstractJavaResourceMethodDispatcher.java, lineNumber=99,
// className=org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher, nativeMethod=false},
// {methodName=invoke, fileName=ResourceMethodInvoker.java, lineNumber=389, className=org.glassfish.jersey.server.model.ResourceMethodInvoker,
// nativeMethod=false},
// {methodName=apply, fileName=ResourceMethodInvoker.java, lineNumber=347, className=org.glassfish.jersey.server.model.ResourceMethodInvoker,
// nativeMethod=false},
// {methodName=apply, fileName=ResourceMethodInvoker.java, lineNumber=102, className=org.glassfish.jersey.server.model.ResourceMethodInvoker,
// nativeMethod=false},
// {methodName=run, fileName=ServerRuntime.java, lineNumber=326, className=org.glassfish.jersey.server.ServerRuntime$2, nativeMethod=false},
// {methodName=call, fileName=Errors.java, lineNumber=271, className=org.glassfish.jersey.internal.Errors$1, nativeMethod=false},
// {methodName=call, fileName=Errors.java, lineNumber=267, className=org.glassfish.jersey.internal.Errors$1, nativeMethod=false},
// {methodName=process, fileName=Errors.java, lineNumber=315, className=org.glassfish.jersey.internal.Errors, nativeMethod=false},
// {methodName=process, fileName=Errors.java, lineNumber=297, className=org.glassfish.jersey.internal.Errors, nativeMethod=false},
// {methodName=process, fileName=Errors.java, lineNumber=267, className=org.glassfish.jersey.internal.Errors, nativeMethod=false},
// {methodName=runInScope, fileName=RequestScope.java, lineNumber=317, className=org.glassfish.jersey.process.internal.RequestScope, nativeMethod=false},
// {methodName=process, fileName=ServerRuntime.java, lineNumber=305, className=org.glassfish.jersey.server.ServerRuntime, nativeMethod=false},
// {methodName=handle, fileName=ApplicationHandler.java, lineNumber=1154, className=org.glassfish.jersey.server.ApplicationHandler, nativeMethod=false},
// {methodName=serviceImpl, fileName=WebComponent.java, lineNumber=473, className=org.glassfish.jersey.servlet.WebComponent, nativeMethod=false},
// {methodName=service, fileName=WebComponent.java, lineNumber=427, className=org.glassfish.jersey.servlet.WebComponent, nativeMethod=false},
// {methodName=service, fileName=ServletContainer.java, lineNumber=388, className=org.glassfish.jersey.servlet.ServletContainer, nativeMethod=false},
// {methodName=service, fileName=ServletContainer.java, lineNumber=341, className=org.glassfish.jersey.servlet.ServletContainer, nativeMethod=false},
// {methodName=service, fileName=ServletContainer.java, lineNumber=228, className=org.glassfish.jersey.servlet.ServletContainer, nativeMethod=false},
// {methodName=service, fileName=HttpServiceRuntimeImpl.java, lineNumber=1271,
// className=org.eclipse.equinox.http.servlet.internal.HttpServiceRuntimeImpl$LegacyServlet, nativeMethod=false},
// {methodName=service, fileName=EndpointRegistration.java, lineNumber=162,
// className=org.eclipse.equinox.http.servlet.internal.registration.EndpointRegistration, nativeMethod=false},
// {methodName=processRequest, fileName=ResponseStateHandler.java, lineNumber=63,
// className=org.eclipse.equinox.http.servlet.internal.servlet.ResponseStateHandler, nativeMethod=false},
// {methodName=doDispatch, fileName=HttpServiceRuntimeImpl.java, lineNumber=413, className=org.eclipse.equinox.http.servlet.internal.HttpServiceRuntimeImpl,
// nativeMethod=false},
// {methodName=doDispatch, fileName=HttpServiceRuntimeImpl.java, lineNumber=358, className=org.eclipse.equinox.http.servlet.internal.HttpServiceRuntimeImpl,
// nativeMethod=false},
// {methodName=doDispatch, fileName=HttpServiceRuntimeImpl.java, lineNumber=204, className=org.eclipse.equinox.http.servlet.internal.HttpServiceRuntimeImpl,
// nativeMethod=false},
// {methodName=processAlias, fileName=ProxyServlet.java, lineNumber=91, className=org.eclipse.equinox.http.servlet.internal.servlet.ProxyServlet,
// nativeMethod=false},
// {methodName=service, fileName=ProxyServlet.java, lineNumber=70, className=org.eclipse.equinox.http.servlet.internal.servlet.ProxyServlet,
// nativeMethod=false},
// {methodName=service, fileName=HttpServlet.java, lineNumber=790, className=javax.servlet.http.HttpServlet, nativeMethod=false},
// {methodName=service, fileName=HttpServerManager.java, lineNumber=356,
// className=org.eclipse.equinox.http.jetty.internal.HttpServerManager$InternalHttpServiceServlet, nativeMethod=false},
// {methodName=handle, fileName=ServletHolder.java, lineNumber=808, className=org.eclipse.jetty.servlet.ServletHolder, nativeMethod=false},
// {methodName=doHandle, fileName=ServletHandler.java, lineNumber=587, className=org.eclipse.jetty.servlet.ServletHandler, nativeMethod=false},
// {methodName=doHandle, fileName=SessionHandler.java, lineNumber=221, className=org.eclipse.jetty.server.session.SessionHandler, nativeMethod=false},
// {methodName=doHandle, fileName=ContextHandler.java, lineNumber=1127, className=org.eclipse.jetty.server.handler.ContextHandler, nativeMethod=false},
// {methodName=doScope, fileName=ServletHandler.java, lineNumber=515, className=org.eclipse.jetty.servlet.ServletHandler, nativeMethod=false},
// {methodName=doScope, fileName=SessionHandler.java, lineNumber=185, className=org.eclipse.jetty.server.session.SessionHandler, nativeMethod=false},
// {methodName=doScope, fileName=ContextHandler.java, lineNumber=1061, className=org.eclipse.jetty.server.handler.ContextHandler, nativeMethod=false},
// {methodName=handle, fileName=ScopedHandler.java, lineNumber=141, className=org.eclipse.jetty.server.handler.ScopedHandler, nativeMethod=false},
// {methodName=handle, fileName=HandlerWrapper.java, lineNumber=97, className=org.eclipse.jetty.server.handler.HandlerWrapper, nativeMethod=false},
// {methodName=handle, fileName=Server.java, lineNumber=497, className=org.eclipse.jetty.server.Server, nativeMethod=false},
// {methodName=handle, fileName=HttpChannel.java, lineNumber=310, className=org.eclipse.jetty.server.HttpChannel, nativeMethod=false},
// {methodName=onFillable, fileName=HttpConnection.java, lineNumber=257, className=org.eclipse.jetty.server.HttpConnection, nativeMethod=false},
// {methodName=run, fileName=AbstractConnection.java, lineNumber=540, className=org.eclipse.jetty.io.AbstractConnection$2, nativeMethod=false},
// {methodName=runJob, fileName=QueuedThreadPool.java, lineNumber=635, className=org.eclipse.jetty.util.thread.QueuedThreadPool, nativeMethod=false},
// {methodName=run, fileName=QueuedThreadPool.java, lineNumber=555, className=org.eclipse.jetty.util.thread.QueuedThreadPool$3, nativeMethod=false},
// {methodName=run, fileName=Thread.java, lineNumber=745, className=java.lang.Thread, nativeMethod=false}
// ],
// serverErrorMessage={message=duplicate key value violates unique constrain...
// }
