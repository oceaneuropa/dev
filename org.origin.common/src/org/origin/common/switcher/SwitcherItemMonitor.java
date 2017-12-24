package org.origin.common.switcher;

public interface SwitcherItemMonitor<ITEM> {

	void start(Switcher<ITEM> switcher);

	void stop(Switcher<ITEM> switcher);

}
