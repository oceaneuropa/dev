package org.origin.common.rest.switcher.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.event.PropertyChangeEvent;
import org.origin.common.event.PropertyChangeListener;
import org.origin.common.event.PropertyChangeListenerSupport;
import org.origin.common.rest.switcher.SwitcherInput;

public class SwitcherInputURIImpl implements SwitcherInput<URI> {

	protected List<URI> baseUriList;
	protected Map<URI, Map<String, Object>> uriProperties = new HashMap<URI, Map<String, Object>>();
	protected PropertyChangeListenerSupport listenerSupport = new PropertyChangeListenerSupport();

	public SwitcherInputURIImpl(List<URI> baseUriList) {
		this.baseUriList = baseUriList;
		if (this.baseUriList == null) {
			this.baseUriList = new ArrayList<URI>();
		}
	}

	protected void checkURI(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI is null.");
		}
	}

	@Override
	public synchronized List<URI> getItems() {
		return this.baseUriList;
	}

	@Override
	public synchronized boolean contains(URI uri) {
		if (uri != null && this.baseUriList.contains(uri)) {
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean add(URI uri) {
		if (uri == null || this.baseUriList.contains(uri)) {
			return false;
		}

		boolean added = this.baseUriList.add(uri);
		if (added) {
			PropertyChangeEvent event = new PropertyChangeEvent(this, SwitcherInput.P_ITEM_ADDED, null, uri);
			notifyPropertyChangeEvent(event);
		}
		return added;
	}

	@Override
	public synchronized boolean remove(URI uri) {
		if (uri == null || this.baseUriList.contains(uri)) {
			return false;
		}
		boolean removed = this.baseUriList.remove(uri);
		if (removed) {
			PropertyChangeEvent event = new PropertyChangeEvent(this, SwitcherInput.P_ITEM_REMOVED, uri, null);
			notifyPropertyChangeEvent(event);
		}
		return removed;
	}

	@Override
	public String[] getPropertyNames(URI uri) {
		checkURI(uri);
		String[] propNames = null;
		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props != null) {
				propNames = props.keySet().toArray(new String[props.size()]);
			}
		}
		if (propNames != null) {
			propNames = new String[0];
		}
		return propNames;
	}

	@Override
	public boolean hasProperty(URI uri, String propName) {
		checkURI(uri);
		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props != null) {
				if (props.containsKey(propName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object getProperty(URI uri, String propName) {
		checkURI(uri);
		Object propValue = null;
		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props != null) {
				propValue = props.get(propName);
			}
		}
		return propValue;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(URI uri, String propName, Class<T> clazz) {
		checkURI(uri);
		T propValue = null;
		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props != null) {
				Object object = props.get(propName);
				if (object != null && clazz.isAssignableFrom(object.getClass())) {
					propValue = (T) object;
				}
			}
		}
		return propValue;
	}

	@Override
	public void setProperty(URI uri, String propName, Object propValue) {
		checkURI(uri);

		if (propValue == null) {
			removeProperty(uri, propName);
			return;
		}

		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props == null) {
				props = new HashMap<String, Object>();
				this.uriProperties.put(uri, props);
			}
			props.put(propName, propValue);

			PropertyChangeEvent event = new PropertyChangeEvent(this, SwitcherInput.P_ITEM_PROPERTY_ADDED, null, propValue);
			event.put("item", uri);
			event.put("prop_name", propName);
			notifyPropertyChangeEvent(event);
		}
	}

	@Override
	public Object removeProperty(URI uri, String propName) {
		checkURI(uri);
		Object propValue = null;
		synchronized (this.uriProperties) {
			Map<String, Object> props = this.uriProperties.get(uri);
			if (props != null) {
				boolean contains = props.containsKey(propName);
				propValue = props.remove(propName);

				if (contains) {
					PropertyChangeEvent event = new PropertyChangeEvent(this, SwitcherInput.P_ITEM_PROPERTY_REMOVED, propValue, null);
					event.put("item", uri);
					event.put("prop_name", propName);
					notifyPropertyChangeEvent(event);
				}
			}
		}

		return propValue;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.listenerSupport.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.listenerSupport.removePropertyChangeListener(listener);
	}

	@Override
	public void notifyPropertyChangeEvent(PropertyChangeEvent event) {
		this.listenerSupport.notifyPropertyChangeEvent(event);
	}

}

// int getIndex();
//
// void setIndex(int index);
//
// protected int index = 0;
//
// @Override
// public int getIndex() {
// return this.index;
// }
//
// @Override
// public void setIndex(int index) {
// this.index = index;
// }
