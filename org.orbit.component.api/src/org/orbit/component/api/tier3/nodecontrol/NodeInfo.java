package org.orbit.component.api.tier3.nodecontrol;

import java.net.URI;
import java.util.Map;

import org.orbit.component.api.RuntimeStatus;

public interface NodeInfo {

	String getId();

	String getName();

	URI getUri();

	Map<String, Object> getAttributes();

	RuntimeStatus getRuntimeStatus();

	Map<String, Object> getRuntimeProperties();

}
