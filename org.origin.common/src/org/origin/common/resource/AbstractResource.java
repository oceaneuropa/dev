package org.origin.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;
import org.origin.common.io.IOUtil;

public abstract class AbstractResource implements Resource {

	protected URI uri;
	protected List<Object> contents;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param uri
	 */
	public AbstractResource(URI uri) {
		this.uri = uri;
		this.contents = new ArrayList<Object>() {
			private static final long serialVersionUID = -8347563649065842011L;

			@Override
			public boolean add(Object element) {
				adaptResource(element);
				return super.add(element);
			}

			@Override
			public void add(int index, Object element) {
				adaptResource(element);
				super.add(index, element);
			}

			@Override
			public boolean addAll(Collection<? extends Object> collection) {
				if (collection != null) {
					for (Iterator<?> itor = collection.iterator(); itor.hasNext();) {
						Object element = itor.next();
						adaptResource(element);
					}
				}
				return super.addAll(collection);
			}

			@Override
			public boolean addAll(int index, Collection<? extends Object> collection) {
				if (collection != null) {
					for (Iterator<?> itor = collection.iterator(); itor.hasNext();) {
						Object element = itor.next();
						adaptResource(element);
					}
				}
				return super.addAll(index, collection);
			}
		};
	}

	/**
	 * 
	 * @param element
	 */
	protected void adaptResource(Object element) {
		if (element instanceof IAdaptable) {
			((IAdaptable) element).adapt(Resource.class, this);
		}
	}

	@Override
	public URI getURI() {
		return this.uri;
	}

	/**
	 * Load file into resource.
	 * 
	 * @param file
	 * @throws IOException
	 */
	// @Override
	public void load(File file) throws IOException {
		InputStream input = new FileInputStream(file);
		try {
			clear();
			doLoad(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(input, true);
		}
	}

	/**
	 * Load resource from input stream.
	 * 
	 * @param input
	 * @throws IOException
	 */
	@Override
	public void load(InputStream input) throws IOException {
		doLoad(input);
	}

	/**
	 * Load input stream into resource.
	 * 
	 * @param input
	 * @throws IOException
	 */
	protected abstract void doLoad(InputStream input) throws IOException;

	/**
	 * Save resource to file.
	 * 
	 * @param file
	 * @throws IOException
	 */
	// @Override
	public void save(File file) throws IOException {
		OutputStream output = new FileOutputStream(file);
		try {
			doSave(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(output, true);
		}
	}

	/**
	 * Save resource to output stream.
	 * 
	 * @param output
	 * @throws IOException
	 */
	@Override
	public void save(OutputStream output) throws IOException {
		doSave(output);
	}

	/**
	 * Save resource to output stream.
	 * 
	 * @param output
	 * @throws IOException
	 */
	protected abstract void doSave(OutputStream output) throws IOException;

	@Override
	public boolean isEmpty() {
		return this.contents.isEmpty();
	}

	@Override
	public List<Object> getContents() {
		return this.contents;
	}

	@SuppressWarnings("unchecked")
	// @Override
	public <ELEMENT> ELEMENT getContent(Class<ELEMENT> elementClass) {
		ELEMENT result = null;
		for (Object content : this.contents) {
			if (elementClass.equals(content.getClass())) {
				result = (ELEMENT) content;
				break;
			}
		}
		if (result == null) {
			for (Object content : this.contents) {
				if (elementClass.isAssignableFrom(content.getClass())) {
					result = (ELEMENT) content;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void clear() {
		this.contents.clear();
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
