package org.origin.common.cluster;

import java.util.List;

public interface PropertyListValueChangeListener {

	void onSetPropertyListAddAll(EventContext context, Object propName, List<Object> list);

	void onSetPropertyListAll(EventContext context, Object propName, Object listItem);

	void onSetPropertyListRemoveAll(EventContext context, Object propName, List<Object> list);

	void onSetPropertyListRemove(EventContext context, Object propName, Object listItem);

	void onSetPropertyListClear(EventContext context, Object propName);

}
