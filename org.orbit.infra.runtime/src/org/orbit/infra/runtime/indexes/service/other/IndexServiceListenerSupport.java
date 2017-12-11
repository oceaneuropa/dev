package org.orbit.infra.runtime.indexes.service.other;

import java.util.ArrayList;
import java.util.List;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.runtime.indexes.service.IndexServiceListener;

public class IndexServiceListenerSupport {

	protected List<IndexServiceListener> listeners = new ArrayList<IndexServiceListener>();

	/**
	 * Add a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void addListener(IndexServiceListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove a IndexServiceListener.
	 * 
	 * @param listener
	 */
	public void removeListener(IndexServiceListener listener) {
		if (listener != null && listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	/**
	 * Get a list of IndexServiceListeners.
	 * 
	 * @return
	 */
	public List<IndexServiceListener> getListeners() {
		return this.listeners;
	}

	/**
	 * Called when an index item is added.
	 * 
	 * @param indexItem
	 */
	public void notifyIndexItemAdded(IndexItem indexItem) {
		for (IndexServiceListener listener : this.listeners) {
			listener.indexItemAdded(indexItem);
		}
	}

	/**
	 * Called when an index item is removed.
	 * 
	 * @param indexItem
	 */
	public void notifyIndexItemRemoved(IndexItem indexItem) {
		for (IndexServiceListener listener : this.listeners) {
			listener.indexItemRemoved(indexItem);
		}
	}

	/**
	 * Called when a property is added.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param propValue
	 */
	public void notifyPropertyAdded(String type, String name, String propName, Object propValue) {
		for (IndexServiceListener listener : this.listeners) {
			listener.propertyAdded(type, name, propName, propValue);
		}
	}

	/**
	 * Called when a property value is changed.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param oldPropValue
	 * @param newPropValue
	 */
	public void notifyPropertyChanged(String type, String name, String propName, Object oldPropValue, Object newPropValue) {
		for (IndexServiceListener listener : this.listeners) {
			listener.propertyChanged(type, name, propName, oldPropValue, newPropValue);
		}
	}

	/**
	 * Called when a property is removed.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 */
	public void notifyPropertyRemoved(String type, String name, String propName) {
		for (IndexServiceListener listener : this.listeners) {
			listener.propertyRemoved(type, name, propName);
		}
	}

}
