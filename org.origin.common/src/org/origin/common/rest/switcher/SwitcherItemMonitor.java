package org.origin.common.rest.switcher;

public interface SwitcherItemMonitor<ITEM> {

	void start(Switcher<ITEM> switcher);

	void stop(Switcher<ITEM> switcher);

}
