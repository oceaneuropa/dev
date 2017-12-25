package org.origin.common.rest.switcher;

import javax.ws.rs.container.ContainerRequestContext;

public interface SwitcherPolicy<ITEM> {

	public static final int MODE_ROUND_ROBIN = 1 << 1;
	public static final int MODE_STICKY = 1 << 2;
	public static final int MODE_WEIGHT = 1 << 3;

	void start(Switcher<ITEM> switcher);

	void stop(Switcher<ITEM> switcher);

	ITEM getNext(ContainerRequestContext requestContext, String methodPath);

}
