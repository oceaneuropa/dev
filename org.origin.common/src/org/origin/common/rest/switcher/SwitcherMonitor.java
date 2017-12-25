package org.origin.common.rest.switcher;

public interface SwitcherMonitor<ITEM> {

	void start(Switcher<ITEM> switcher);

	void stop(Switcher<ITEM> switcher);

	SwitcherItemMonitor<ITEM> createItemMonitor(ITEM item);

}
