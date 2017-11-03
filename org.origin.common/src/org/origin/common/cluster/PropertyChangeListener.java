package org.origin.common.cluster;

import java.util.Map;

public interface PropertyChangeListener {

	void onSetProperties(EventContext context, Map<Object, Object> properties);

	void onSetProperty(EventContext context, Object key, Object value);

	void onRemoveProperty(EventContext context, Object key);

	void onClearProperty(EventContext context);

}
