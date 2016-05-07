package org.origin.mgm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.query.IndexItemQuery;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.IndexServiceListener;
import org.origin.mgm.service.IndexServiceListenerSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class IndexServiceImpl implements IndexService {

	protected List<IndexItem> indexItems = new ArrayList<IndexItem>();

	protected IndexServiceListenerSupport listenerSupport = new IndexServiceListenerSupport();
	protected BundleContext bundleContext;

	protected ServiceRegistration<?> serviceReg;

	/**
	 * 
	 * @param bundleContext
	 */
	public IndexServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		// Register as a IndexService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(IndexService.class, this, props);
	}

	public void stop() {
		// Unregister the IndexService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	/**
	 * Get a list of index items.
	 * 
	 * @return
	 * @throws IndexServiceException
	 */
	@Override
	public List<IndexItem> getIndexItems() throws IndexServiceException {
		return this.indexItems;
	}

	/**
	 * Get a list of index items by type.
	 * 
	 * @param type
	 * @return
	 * @throws IndexServiceException
	 */
	@Override
	public List<IndexItem> getIndexItems(String type) throws IndexServiceException {
		if (type == null) {
			return getIndexItems();
		}
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		for (IndexItem indexItem : this.indexItems) {
			if (type.equals(indexItem.getType())) {
				indexItems.add(indexItem);
			}
		}
		return indexItems;
	}

	/**
	 * Get a list of index items by query.
	 * 
	 * @param indexItemQuery
	 * @return
	 * @throws IndexServiceException
	 */
	@Override
	public List<IndexItem> getIndexItems(IndexItemQuery serviceQuery) throws IndexServiceException {
		return null;
	}

	/**
	 * Check whether an item is indexed.
	 * 
	 * @param type
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	@Override
	public boolean isIndexed(String type, String name) throws IndexServiceException {
		if (type == null) {
			type = IndexItem.DEFAULT_TYPE;
		}
		for (IndexItem indexItem : this.indexItems) {
			if (indexItem.getType().equals(type) && indexItem.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create an index item.
	 * 
	 * @param type
	 * @param name
	 * @param props
	 *            e.g. description, url
	 * @throws IndexServiceException
	 */
	@Override
	public void createIndexItem(String namespace, String name, Map<String, Object> props) throws IndexServiceException {
		if (isIndexed(namespace, name)) {
			// Service is already registered.
			if (props != null) {
				// Update existing service registry entry's properties map.
				for (Iterator<String> nameItor = props.keySet().iterator(); nameItor.hasNext();) {
					String propName = nameItor.next();
					Object propValue = props.get(propName);

					if (hasProperty(namespace, name, propName)) {
						// Property already exists. Update property value only when necessary.
						Object oldPropValue = getProperty(namespace, name, propName);
						if ((oldPropValue == null && propValue != null) || (oldPropValue != null && !oldPropValue.equals(propValue))) {
							setProperty(namespace, name, propName, propValue);
						}
					} else {
						// Property doesn't exist yet.
						setProperty(namespace, name, propName, propValue);
					}
				}
			}

		} else {
			// Service is not registered yet
			// QName qname = IndexItem.getQName(namespace, name);
			// IndexItem entry = new IndexItem(null, namespace, name, props);
			// this.indexItems.put(qname, entry);

			// Notify service added event
			// this.listenerSupport.notifyIndexItemAdded(entry);
		}
	}

	/**
	 * Remove an index item.
	 * 
	 * @param type
	 * @param name
	 * @throws IndexServiceException
	 */
	public void removeIndexItem(String type, String name) throws IndexServiceException {
		if (isIndexed(type, name)) {
			// QName qname = IndexItem.getQName(type, name);
			// IndexItem entry = this.indexItems.remove(qname);
			//
			// // Notify service removed event
			// this.listenerSupport.notifyIndexItemRemoved(entry);
		}
	}

	/**
	 * Get all properties names of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public String[] getPropertyNames(String namespace, String name) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null) {
		// return entry.getProperties().keySet().toArray(new String[entry.getProperties().size()]);
		// }
		return new String[] {};
	}

	/**
	 * Get the properties of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @return
	 * @throws IndexServiceException
	 */
	public Map<String, Object> getProperties(String namespace, String name) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null) {
		// return entry.getProperties();
		// }
		return Collections.emptyMap();
	}

	/**
	 * Check whether a property of a service is available.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @return
	 * @throws IndexServiceException
	 */
	public boolean hasProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null && entry.getProperties().containsKey(propName)) {
		// return true;
		// }
		return false;
	}

	/**
	 * Get the property value of a service.
	 * 
	 * @param namespace
	 * @param name
	 * @param propName
	 * @return
	 * @throws IndexServiceException
	 */
	public Object getProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		// return entry.getProperties().get(propName);
		// }
		return null;
	}

	/**
	 * Set the property of a service.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @param propValue
	 *            property value
	 * @throws IndexServiceException
	 */
	public void setProperty(String namespace, String name, String propName, Object propValue) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		//
		// if (entry.getProperties().containsKey(propName)) {
		// Object oldPropValue = entry.getProperties().get(propName);
		//
		// if ((oldPropValue == null && propValue != null) || (oldPropValue != null && !oldPropValue.equals(propValue))) {
		// entry.getProperties().put(propName, propValue);
		//
		// // Notify service property changed event
		// this.listenerSupport.notifyPropertyChanged(namespace, name, propName, oldPropValue, propValue);
		// }
		//
		// } else {
		// entry.getProperties().put(propName, propValue);
		//
		// // Notify service property added event
		// this.listenerSupport.notifyPropertyAdded(namespace, name, propName, propValue);
		// }
		// }
	}

	/**
	 * Remove the property of a service.
	 * 
	 * @param namespace
	 *            namespace of the service
	 * @param name
	 *            name of the service
	 * @param propName
	 *            property name
	 * @throws IndexServiceException
	 */
	public void removeProperty(String namespace, String name, String propName) throws IndexServiceException {
		QName qname = IndexItem.getQName(namespace, name);
		// IndexItem entry = this.indexItems.get(qname);
		// if (entry != null && entry.getProperties() != null && propName != null) {
		// entry.getProperties().remove(propName);
		//
		// // Notify service property removed event
		// this.listenerSupport.notifyPropertyRemoved(namespace, name, propName);
		// }
	}

	/**
	 * Add a ServiceRegistryListener.
	 * 
	 * @param listener
	 */
	public void addListener(IndexServiceListener listener) {
		listenerSupport.addListener(listener);
	}

	/**
	 * Remove a ServiceRegistryListener.
	 * 
	 * @param listener
	 */
	public void removeListener(IndexServiceListener listener) {
		listenerSupport.removeListener(listener);
	}

}
