package org.origin.common.workingcopy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <RES>
 * @param <ELEMENT>
 */
public abstract class AbstractWorkingCopy<RES extends Resource, ELEMENT extends Object> implements WorkingCopy<ELEMENT> {

	protected File file;
	protected WorkingCopyFactory<ELEMENT> factory;

	protected RES resource;
	protected ELEMENT rootElement;
	protected transient String lock = "lock";

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param file
	 */
	public AbstractWorkingCopy(File file) {
		this.file = file;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	/**
	 * 
	 * @param workingCopyFactory
	 */
	public void setFactory(WorkingCopyFactory<ELEMENT> workingCopyFactory) {
		this.factory = workingCopyFactory;
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized WorkingCopyFactory<ELEMENT> getFactory() {
		if (this.factory == null) {
			this.factory = getAdapter(WorkingCopyFactory.class);
		}
		return this.factory;
	}

	// -----------------------------------------------------------------------------
	// Load
	// -----------------------------------------------------------------------------
	@Override
	public synchronized boolean isLoaded() {
		return this.rootElement != null ? true : false;
	}

	@Override
	public synchronized void reload() throws IOException {
		boolean wasLoaded = isLoaded();
		this.rootElement = null;

		if (wasLoaded) {
			getRootElement();
		}
	}

	@Override
	public ELEMENT getRootElement() {
		if (this.rootElement == null) {
			synchronized (lock) {
				if (this.rootElement == null) {
					if (this.resource == null) {
						this.resource = createResource(this.file);
					}
					if (this.resource != null) {
						InputStream input = null;
						try {
							input = new FileInputStream(this.file);
							this.resource.load(input);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							IOUtil.closeQuietly(input, true);
						}
						this.rootElement = getRootElement(this.resource);
					}
				}
			}
		}
		return this.rootElement;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getRootElement(Class<T> elementClass) {
		T result = null;
		Object element = getRootElement();
		if (element != null && elementClass.isAssignableFrom(element.getClass())) {
			result = (T) element;
		}
		return result;
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected RES createResource(File file) {
		RES resource = null;
		WorkingCopyFactory<ELEMENT> factory = getFactory();
		if (factory instanceof ResourceFactory /* && factory.isSupported(file) */) {
			resource = ((ResourceFactory<RES>) factory).createResource(file);
		}
		return resource;
	}

	/**
	 * 
	 * @param resource
	 * @return
	 */
	protected abstract ELEMENT getRootElement(RES resource);

	// -----------------------------------------------------------------------------
	// Save
	// -----------------------------------------------------------------------------
	@Override
	public synchronized void save() throws IOException {
		if (isLoaded() && this.resource != null) {
			doSave(this.resource, this.file);
		}
	}

	/**
	 * Save root element to file.
	 * 
	 * @param resource
	 * @param file
	 * @throws IOException
	 */
	protected void doSave(Resource resource, File file) throws IOException {
		if (resource != null) {
			OutputStream output = null;
			try {
				output = new FileOutputStream(file);
				resource.save(output);
			} finally {
				IOUtil.closeQuietly(output, true);
			}
		}
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
