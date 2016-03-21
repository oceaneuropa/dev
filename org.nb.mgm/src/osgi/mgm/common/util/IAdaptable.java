package osgi.mgm.common.util;

/**
 * An interface for an adaptable object.
 */
public interface IAdaptable {

	/**
	 * Returns an object which is an instance of the given class. Returns null if no such object can be found.
	 * 
	 * @param adapter
	 * @return
	 */
	public <T> T getAdapter(Class<T> adapter);

}
