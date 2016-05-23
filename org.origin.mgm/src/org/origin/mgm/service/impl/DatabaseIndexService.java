package org.origin.mgm.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.xml.namespace.QName;

import org.origin.common.jdbc.DatabaseUtil;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.persistence.IndexItemDataTableHandler;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;
import org.origin.mgm.service.IndexService;
import org.origin.mgm.service.IndexServiceListener;
import org.origin.mgm.service.IndexServiceListenerSupport;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class DatabaseIndexService implements IndexService {

	protected List<IndexItem> indexItems = new ArrayList<IndexItem>();
	protected IndexItemDataTableHandler dataTableHandler = IndexItemDataTableHandler.INSTANCE;
	protected IndexItemRevisionTableHandler logTableHandler = IndexItemRevisionTableHandler.INSTANCE;

	protected IndexServiceListenerSupport listenerSupport = new IndexServiceListenerSupport();
	protected BundleContext bundleContext;
	protected DatabaseIndexServiceConfiguration indexServiceConfig;

	protected ServiceRegistration<?> serviceReg;

	protected int cachedRevisionId = 0;
	protected ReadWriteLock rwLock = new ReentrantReadWriteLock();

	/**
	 * 
	 * @param bundleContext
	 * @param indexServiceConfig
	 */
	public DatabaseIndexService(BundleContext bundleContext, DatabaseIndexServiceConfiguration indexServiceConfig) {
		this.bundleContext = bundleContext;
		this.indexServiceConfig = indexServiceConfig;
	}

	public void start() {
		System.out.println("IndexServiceImpl.start()");

		// Register as a IndexService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(IndexService.class, this, props);
	}

	public void stop() {
		System.out.println("IndexServiceImpl.stop()");

		// Unregister the IndexService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}
	}

	public synchronized void synchronize() {
		System.out.println("IndexServiceImpl.synchronize()");

		// get the max revision number from the log table
		int maxRevisionId = 0;
		Connection conn = indexServiceConfig.getConnection();
		try {
			maxRevisionId = logTableHandler.getMaxRevisionId(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		System.out.println("maxRevisionId = " + maxRevisionId);
		System.out.println("cachedRevisionId = " + cachedRevisionId);

		if (cachedRevisionId <= 0) {
			// data is not cached yet
			reloadIndexItems();

		} else {
			// data have already been cached
			if (cachedRevisionId == maxRevisionId) {
				// The cached data is update to date. No need to sync with database.

			} else if (cachedRevisionId < maxRevisionId) {
				// The cached data is left behind the true data. Need to sync with database.
				// Get the logs [cachedRevision + 1, maxRevision] and use the logs to update the cached data.

			} else if (cachedRevisionId > maxRevisionId) {
				// The cached data is in front of the true data.
				// Revert the cached data from cachedRevision to maxRevision. --- how to do that?

				// Note:
				// To support revert. It requires:

				// (1) Every log contains a atomic action (with command name and command arguments)
				// --- that means the set of commands and arguments need to be clearly defined.

				// (2) Every log contains a undo action (with command name and command arguments) that can undo the changes made by the action.
				// --- that means a Command model for each write-action that can create, modify, delete index item or its properties need to
				// be defined and implemented. (similar to the Eclipse refactoring Change model)
				// --- and that a simple framework for executing the Commands are needed.
			}
		}
	}

	protected void reloadIndexItems() {

	}

	protected void keepupWithIndexItems(int fromRevision, int toRevision) {

	}

	@Override
	public List<IndexItem> getIndexItems() throws IndexServiceException {
		return this.indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String namespace) throws IndexServiceException {
		if (namespace == null) {
			return getIndexItems();
		}
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		for (IndexItem indexItem : this.indexItems) {
			if (namespace.equals(indexItem.getNamespace())) {
				indexItems.add(indexItem);
			}
		}
		return indexItems;
	}

	@Override
	public List<IndexItem> getIndexItems(String indexProviderId, String namespace) throws IndexServiceException {
		if (namespace == null) {
			return getIndexItems();
		}
		List<IndexItem> indexItems = new ArrayList<IndexItem>();
		for (IndexItem indexItem : this.indexItems) {
			if (namespace.equals(indexItem.getNamespace())) {
				indexItems.add(indexItem);
			}
		}
		return indexItems;
	}

	@Override
	public void createIndexItem(String indexProviderId, String namespace, String name) throws IndexServiceException {

	}

	/**
	 * Remove an index item.
	 * 
	 * @param type
	 * @param name
	 * @throws IndexServiceException
	 */
	public void removeIndexItem(String type, String name) throws IndexServiceException {

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
