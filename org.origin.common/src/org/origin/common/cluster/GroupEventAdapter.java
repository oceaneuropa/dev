package org.origin.common.cluster;

import java.util.List;
import java.util.Map;

public class GroupEventAdapter implements EventListener, PropertyChangeListener, PropertyMapValueChangeListener, PropertyListValueChangeListener {

	@Override
	public void onReceiveEvent(EventContext context, Event event) {

	}

	@Override
	public void onSetProperties(EventContext context, Map<Object, Object> properties) {

	}

	@Override
	public void onSetProperty(EventContext context, Object key, Object value) {

	}

	@Override
	public void onRemoveProperty(EventContext context, Object key) {

	}

	@Override
	public void onClearProperty(EventContext context) {

	}

	@Override
	public void onSetPropertyMapPutAll(EventContext context, Object propName, Map<Object, Object> map) {

	}

	@Override
	public void onSetPropertyMapPut(EventContext context, Object propName, Object mapKey, Object mapValue) {

	}

	@Override
	public void onSetPropertyMapRemove(EventContext context, Object propName, Object mapKey) {

	}

	@Override
	public void onSetPropertyMapClear(EventContext context, Object propName) {

	}

	@Override
	public void onSetPropertyListAddAll(EventContext context, Object propName, List<Object> list) {

	}

	@Override
	public void onSetPropertyListAll(EventContext context, Object propName, Object listItem) {

	}

	@Override
	public void onSetPropertyListRemoveAll(EventContext context, Object propName, List<Object> list) {

	}

	@Override
	public void onSetPropertyListRemove(EventContext context, Object propName, Object listItem) {

	}

	@Override
	public void onSetPropertyListClear(EventContext context, Object propName) {

	}

}
