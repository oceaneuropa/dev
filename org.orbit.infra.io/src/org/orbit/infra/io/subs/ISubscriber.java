package org.orbit.infra.io.subs;

import java.util.Map;

public interface ISubscriber {

	String getId();

	String getType();

	String getInstanceId();

	Map<String, Object> getProperties();

}
