package org.orbit.infra.io.subscription;

import java.util.Map;

public interface ISubscribable {

	String getId();

	String getType();

	String getInstanceId();

	String getServerId();

	String getServerURL();

	Map<String, Object> getProperties();

}
