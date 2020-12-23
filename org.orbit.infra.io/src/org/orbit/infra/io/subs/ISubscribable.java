package org.orbit.infra.io.subs;

import java.util.Map;

public interface ISubscribable {

	String getId();

	String getType();

	String getInstanceId();

	String getServerId();

	String getServerURL();

	Map<String, Object> getProperties();

}
