package org.orbit.component.api.tier3.domain;

import org.orbit.component.api.RuntimeStatus;

/**
 * Platform configuration
 * 
 */
public interface PlatformConfig {

	String getMachineId();

	String getId();

	String getName();

	String getHome();

	String getHostURL();

	String getContextRoot();

	RuntimeStatus getRuntimeStatus();

}
