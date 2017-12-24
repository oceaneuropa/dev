package org.origin.common.switcher;

public interface SwitcherPolicy<ITEM> {

	void start(Switcher<ITEM> switcher);

	void stop(Switcher<ITEM> switcher);

	ITEM getNext(String methodPath);

}
