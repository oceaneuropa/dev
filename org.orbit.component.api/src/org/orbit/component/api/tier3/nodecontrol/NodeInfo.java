package org.orbit.component.api.tier3.nodecontrol;

import java.net.URI;
import java.util.Map;

public interface NodeInfo {

	String getId();

	String getName();

	URI getUri();

	Map<String, Object> getAttributes();

	NodeStatus getStatus();

}
