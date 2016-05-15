package org.origin.common.adapter;

/**
 * An interface for an adaptable object.
 */
public interface IAdaptable {

	/**
	 * 
	 * @param clazz
	 * @param object
	 */
	public <T> void adapt(Class<T> clazz, T object);

	/**
	 * Returns an object which is an instance of the given class. Returns null if no such object can be found.
	 * 
	 * @param adapter
	 * @return
	 */
	public <T> T getAdapter(Class<T> adapter);

}
