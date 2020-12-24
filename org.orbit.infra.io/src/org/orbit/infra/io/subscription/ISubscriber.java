package org.orbit.infra.io.subscription;

import java.util.Map;

public interface ISubscriber {

	String getId();

	String getType();

	String getInstanceId();

	Map<String, Object> getProperties();

}
