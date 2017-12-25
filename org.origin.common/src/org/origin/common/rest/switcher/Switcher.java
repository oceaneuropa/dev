package org.origin.common.rest.switcher;

import javax.ws.rs.container.ContainerRequestContext;

public interface Switcher<ITEM> {

	public static final String PROP_STATUS = "status"; // available/unavailable (null means available)
	public static final String PROP_ENABLED = "enabled"; // true/false (null means true)
	public static final String PROP_CLIENT_ID = "clientId"; //

	SwitcherInput<ITEM> getInput();

	void setInput(SwitcherInput<ITEM> input);

	SwitcherPolicy<ITEM> getPolicy();

	void setPolicy(SwitcherPolicy<ITEM> policy);

	SwitcherMonitor<ITEM> getMonitor();

	void setMonitor(SwitcherMonitor<ITEM> monitor);

	void start();

	void stop();

	ITEM getNext(ContainerRequestContext requestContext, String methodPath);

	ITEM getNext(ContainerRequestContext requestContext, String methodPath, int retry, long interval);

}
