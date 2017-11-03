package org.origin.common.cluster;

import java.util.Map;

public interface PropertyMapValueChangeListener {

	void onSetPropertyMapPutAll(EventContext context, Object propName, Map<Object, Object> map);

	void onSetPropertyMapPut(EventContext context, Object propName, Object mapKey, Object mapValue);

	void onSetPropertyMapRemove(EventContext context, Object propName, Object mapKey);

	void onSetPropertyMapClear(EventContext context, Object propName);

}
