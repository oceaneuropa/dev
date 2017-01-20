package org.origin.common.resource.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.io.IOUtil;
import org.origin.common.resource.RList;
import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactoryRegistry;
import org.origin.common.resource.URIConverter;

public abstract class ResourceImpl implements Resource {

	protected URI uri;
	protected URIConverter uriConverter;
	protected List<Object> contents;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param file
	 */
	public ResourceImpl(File file) {
		this(file.toURI());
	}

	/**
	 * 
	 * @param uri
	 */
	public ResourceImpl(URI uri) {
		this.uri = uri;
		this.contents = new RList<Object>(this);
	}

	@Override
	public URI getURI() {
		return this.uri;
	}

	@Override
	public synchronized URIConverter getURIConverter() {
		return (this.uriConverter == null) ? ResourceFactoryRegistry.INSTANCE.getDefaultURIConverter() : this.uriConverter;
	}

	@Override
	public synchronized void setURIConverter(URIConverter uriConverter) {
		this.uriConverter = uriConverter;
	}

	@Override
	public boolean exists() throws IOException {
		URIConverter uriConverter = getURIConverter();
		return (uriConverter != null) ? uriConverter.exists(this.uri) : false;
	}

	@Override
	public boolean createNewResource() throws IOException {
		URIConverter uriConverter = getURIConverter();
		return (uriConverter != null) ? uriConverter.createNewResource(this.uri) : false;
	}

	@Override
	public void load() throws IOException {
		URIConverter uriConverter = getURIConverter();
		if (uriConverter != null && uriConverter.exists(this.uri)) {
			InputStream input = null;
			try {
				input = uriConverter.createInputStream(this.uri);
				load(input);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtil.closeQuietly(input, true);
			}
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
		if (input != null) {
			// clear contents
			clear();
			// load contents from input stream
			doLoad(input);
		}
	}

	/**
	 * Load input stream into resource.
	 * 
	 * @param input
	 * @throws IOException
	 */
	protected abstract void doLoad(InputStream input) throws IOException;

	@Override
	public void save() throws IOException {
		URIConverter uriConverter = getURIConverter();
		if (uriConverter != null) {
			OutputStream output = null;
			try {
				if (!exists()) {
					uriConverter.createNewResource(this.uri);
				}

				output = uriConverter.createOutputStream(this.uri);
				save(output);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtil.closeQuietly(output, true);
			}
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
	public boolean delete() throws IOException {
		URIConverter uriConverter = getURIConverter();
		if (uriConverter != null) {
			return uriConverter.delete(this.uri);
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return this.contents.isEmpty();
	}

	@Override
	public List<Object> getContents() {
		return this.contents;
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

// /**
// * Load file into resource.
// *
// * @param file
// * @throws IOException
// */
// // @Override
// public void load(File file) throws IOException {
// InputStream input = null;
// try {
// input = new FileInputStream(file);
// load(input);
// } catch (IOException e) {
// e.printStackTrace();
// } finally {
// IOUtil.closeQuietly(input, true);
// }
// }

// /**
// * Save resource to file.
// *
// * @param file
// * @throws IOException
// */
// // @Override
// public void save(File file) throws IOException {
// OutputStream output = null;
// try {
// output = new FileOutputStream(file);
// save(output);
// } catch (IOException e) {
// e.printStackTrace();
// } finally {
// IOUtil.closeQuietly(output, true);
// }
// }
