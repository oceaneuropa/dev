package other.orbit.infra.runtime.indexes.service;

import org.orbit.infra.model.indexes.IndexItem;

public interface IndexServiceListener {

	/**
	 * Return the type of the index items this listener is listening to.
	 * 
	 * Return a type to get callback when an index item or the property with that type is changed.
	 * 
	 * Return null to get callback when an index item or the property with any type is changed.
	 * 
	 * @return
	 */
	public String getType();

	/**
	 * Called when an index item is added.
	 * 
	 * @param indexItem
	 */
	public void indexItemAdded(IndexItem indexItem);

	/**
	 * Called when a service is removed.
	 * 
	 * @param indexItem
	 */
	public void indexItemRemoved(IndexItem indexItem);

	/**
	 * Called when a property is added.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param propValue
	 */
	public void propertyAdded(String type, String name, String propName, Object propValue);

	/**
	 * Called when a property value is changed.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 * @param oldPropValue
	 * @param newPropValue
	 */
	public void propertyChanged(String type, String name, String propName, Object oldPropValue, Object newPropValue);

	/**
	 * Called when a property is removed.
	 * 
	 * @param type
	 * @param name
	 * @param propName
	 */
	public void propertyRemoved(String type, String name, String propName);

}
