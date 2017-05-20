package org.origin.common.workingcopy;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactory;

/**
 *
 * @param <RES>
 */
public abstract class AbstractWorkingCopy<RES extends Resource> implements WorkingCopy {

	protected URI uri;
	protected WorkingCopyFactory factory;
	protected RES resource;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param uri
	 */
	public AbstractWorkingCopy(URI uri) {
		this.uri = uri;
	}

	/**
	 * Get resource URI.
	 * 
	 * @return
	 */
	@Override
	public URI getURI() {
		return this.uri;
	}

	/**
	 * Set working copy factory.
	 * 
	 * @param factory
	 */
	@Override
	public void setFactory(WorkingCopyFactory factory) {
		this.factory = factory;
	}

	/**
	 * Get working copy factory.
	 * 
	 * @return
	 */
	@Override
	public synchronized WorkingCopyFactory getFactory() {
		if (this.factory == null) {
			this.factory = getAdapter(WorkingCopyFactory.class);
		}
		return this.factory;
	}

	// -----------------------------------------------------------------------------
	// Resource instance
	// -----------------------------------------------------------------------------
	/**
	 * Get the resource.
	 * 
	 * @return
	 */
	@Override
	public synchronized RES getResource() throws IOException {
		if (this.resource == null) {
			// Create new resource instance.
			this.resource = createResource(this.uri);

			// Reload the resource if available.
			if (this.resource != null && this.resource.exists()) {
				reload();
			}
		}
		return this.resource;
	}

	/**
	 * Create new resource instance.
	 * 
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected RES createResource(URI uri) throws IOException {
		RES resource = null;
		WorkingCopyFactory factory = getFactory();
		if (factory instanceof ResourceFactory /* && factory.isSupported(file) */) {
			resource = ((ResourceFactory<RES>) factory).createResource(uri);
		}
		return resource;
	}

	// -----------------------------------------------------------------------------
	// Load
	// -----------------------------------------------------------------------------
	@Override
	public synchronized boolean isLoaded() {
		return this.resource != null ? true : false;
	}

	@Override
	public synchronized void reload() throws IOException {
		if (this.resource != null) {
			this.resource.clear();
			this.resource.load();
		} else {
			getResource();
		}
	}

	@Override
	public synchronized void unload() {
		if (this.resource != null) {
			this.resource.clear();
			this.resource = null;
		}
	}

	// -----------------------------------------------------------------------------
	// Save
	// -----------------------------------------------------------------------------
	@Override
	public synchronized void save() throws IOException {
		if (this.resource != null) {
			this.resource.save();
		}
	}

	// -----------------------------------------------------------------------------
	// Contents
	// -----------------------------------------------------------------------------
	@Override
	public <T> T getRootElement(Class<T> elementClass) throws IOException {
		T result = null;
		RES resource = getResource();
		if (resource != null) {
			result = getRootElement(resource, elementClass);
		}
		return result;
	}

	/**
	 * Subclass can override this method to get specific root element.
	 * 
	 * @param resource
	 * @param elementClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getRootElement(RES resource, Class<T> elementClass) throws IOException {
		T result = null;
		if (resource != null) {
			List<Object> contents = resource.getContents();
			if (contents != null && !contents.isEmpty()) {
				for (Object element : contents) {
					if (element != null && elementClass.isAssignableFrom(element.getClass())) {
						result = (T) element;
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Get the contents.
	 *
	 * @return
	 * @throws IOException
	 */
	@Override
	public List<Object> getContents() throws IOException {
		List<Object> result = new ArrayList<Object>();
		RES resource = getResource();
		if (resource != null) {
			List<Object> contents = resource.getContents();
			if (contents != null && !contents.isEmpty()) {
				result.addAll(contents);
			}
		}
		return result;
	}

	/**
	 * Get the contents of specified class.
	 * 
	 * @param elementClass
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getContents(Class<T> clazz) throws IOException {
		List<T> result = new ArrayList<T>();
		RES resource = getResource();
		if (resource != null) {
			List<Object> contents = resource.getContents();
			if (contents != null) {
				for (Object element : contents) {
					if (clazz.equals(element.getClass()) || clazz.isAssignableFrom(element.getClass())) {
						result.add((T) element);
					}
				}
			}
		}
		return result;
	}

	/** implement IAdaptable interface */
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
