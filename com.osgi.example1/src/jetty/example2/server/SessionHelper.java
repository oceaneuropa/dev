package jetty.example2.server;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

public class SessionHelper {

	public static SessionHelper INSTANCE = new SessionHelper();

	/**
	 * 
	 * @param session
	 */
	public void print(Session session) {
		URI uri = session.getRequestURI();
		String query = session.getQueryString();
		Map<String, String> pathParams = session.getPathParameters();
		Map<String, List<String>> requestParamsMap = session.getRequestParameterMap();
		Map<String, Object> userProps = session.getUserProperties();

		System.out.println("------------------------------------------------------------------------");
		System.out.println("uri = " + uri);
		System.out.println("query = " + query);

		if (pathParams != null) {
			System.out.println("pathParams =");
			for (Iterator<String> pathParamItor = pathParams.keySet().iterator(); pathParamItor.hasNext();) {
				String pathParamName = pathParamItor.next();
				String pathParamValue = pathParams.get(pathParamName);
				System.out.println("\t" + pathParamName + " = " + pathParamValue);
			}
		} else {
			System.out.println("pathParams = null");
		}

		if (requestParamsMap != null) {
			System.out.println("requestParamsMap =");
			for (Iterator<String> requestParamItor = requestParamsMap.keySet().iterator(); requestParamItor.hasNext();) {
				String requestParamName = requestParamItor.next();
				List<String> requestParamValues = requestParamsMap.get(requestParamName);
				String requestParamValuesStr = (requestParamValues != null) ? requestParamValues.toString() : "(empty)";
				System.out.println("\t" + requestParamName + " = " + requestParamValuesStr);
			}
		} else {
			System.out.println("requestParamsMap = null");
		}

		if (pathParams != null) {
			System.out.println("userProps =");
			for (Iterator<String> userPropItor = userProps.keySet().iterator(); userPropItor.hasNext();) {
				String propName = userPropItor.next();
				Object propValue = userProps.get(propName);
				System.out.println("\t" + propName + " = " + propValue);
			}
		} else {
			System.out.println("userProps = null");
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println();
	}

	public void printUserProperties(Session session) {
		for (Iterator<String> userPropsItor = session.getUserProperties().keySet().iterator(); userPropsItor.hasNext();) {
			String propName = userPropsItor.next();
			Object propValue = session.getUserProperties().get(propName);
			System.out.println("\t" + propName + " = " + propValue);
		}
	}

}
