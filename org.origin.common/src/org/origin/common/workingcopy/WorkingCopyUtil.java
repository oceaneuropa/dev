package org.origin.common.workingcopy;

import java.io.File;
import java.net.URI;

import org.origin.common.resource.RObject;
import org.origin.common.resource.Resource;
import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;

public class WorkingCopyUtil {

	/**
	 * Check whether there is a working copy for a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static boolean hasWorkingCopy(File file) {
		checkFile(file);
		return hasWorkingCopy(file.toURI());
	}

	/**
	 * Check whether there is a working copy for a URI.
	 * 
	 * @param uri
	 * @return
	 */
	public synchronized static boolean hasWorkingCopy(URI uri) {
		checkURI(uri);

		WorkingCopyFactory factory = getWorkingCopyFactory(uri);
		if (factory != null && factory.hasWorkingCopy(uri)) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether there is a working copy for a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static <WCF extends WorkingCopyFactory> boolean hasWorkingCopy(File file, Class<WCF> factoryClass) {
		checkFile(file);
		return hasWorkingCopy(file.toURI(), factoryClass);
	}

	/**
	 * Check whether there is a working copy for a URI.
	 * 
	 * @param uri
	 * @return
	 */
	public synchronized static <WCF extends WorkingCopyFactory> boolean hasWorkingCopy(URI uri, Class<WCF> factoryClass) {
		checkURI(uri);

		WorkingCopyFactory factory = getWorkingCopyFactory(uri, factoryClass);
		if (factory != null && factory.hasWorkingCopy(uri)) {
			return true;
		}
		return false;
	}

	/**
	 * Get the working copy of a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static WorkingCopy getWorkingCopy(File file) {
		checkFile(file);
		return getWorkingCopy(file.toURI());
	}

	/**
	 * Get the working copy of a URI.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static WorkingCopy getWorkingCopy(URI uri) {
		checkURI(uri);

		WorkingCopy workingCopy = null;
		WorkingCopyFactory factory = getWorkingCopyFactory(uri);
		if (factory != null) {
			workingCopy = factory.getWorkingCopy(uri);
		}
		return workingCopy;
	}

	/**
	 * Get the working copy of a file.
	 * 
	 * @param file
	 * @param factoryClass
	 * @param wcClass
	 * @return
	 */
	public synchronized static <WCF extends WorkingCopyFactory, WC extends WorkingCopy> WC getWorkingCopy(File file, Class<WCF> factoryClass, Class<WC> wcClass) {
		checkFile(file);
		return (WC) getWorkingCopy(file.toURI(), factoryClass, wcClass);
	}

	/**
	 * Get the working copy of a URI.
	 * 
	 * @param uri
	 * @param factoryClass
	 * @param wcClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <WCF extends WorkingCopyFactory, WC extends WorkingCopy> WC getWorkingCopy(URI uri, Class<WCF> factoryClass, Class<WC> wcClass) {
		checkURI(uri);

		WorkingCopy workingCopy = null;
		WorkingCopyFactory factory = getWorkingCopyFactory(uri, factoryClass);
		if (factory != null) {
			WorkingCopy currWorkingCopy = factory.getWorkingCopy(uri);
			if (currWorkingCopy != null && wcClass.isAssignableFrom(currWorkingCopy.getClass())) {
				workingCopy = currWorkingCopy;
			}
		}
		return (WC) workingCopy;
	}

	/**
	 * Get the working copy of a RObject (a resource object).
	 * 
	 * @param rObject
	 * @return
	 */
	public synchronized static WorkingCopy getWorkingCopy(RObject rObject) {
		WorkingCopy workingCopy = null;
		if (rObject != null) {
			Resource resource = rObject.eResource();
			if (resource != null) {
				URI uri = resource.getURI();
				if (uri != null) {
					workingCopy = getWorkingCopy(uri);
				}
			}
		}
		return workingCopy;
	}

	/**
	 * Remove the working copy of a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static WorkingCopy removeWorkingCopy(File file) {
		checkFile(file);
		removeWorkingCopy(file.toURI());
		return null;
	}

	/**
	 * Remove the working copy of a URI.
	 * 
	 * @param uri
	 * @return
	 */
	public synchronized static WorkingCopy removeWorkingCopy(URI uri) {
		checkURI(uri);

		WorkingCopyFactory factory = getWorkingCopyFactory(uri);
		if (factory != null && factory.hasWorkingCopy(uri)) {
			return factory.removeWorkingCopy(uri);
		}
		return null;
	}

	/**
	 * Remove the working copy of a file.
	 * 
	 * @param file
	 * @param factoryClass
	 * @return
	 */
	public synchronized static <WCF extends WorkingCopyFactory> WorkingCopy removeWorkingCopy(File file, Class<WCF> factoryClass) {
		checkFile(file);
		return removeWorkingCopy(file.toURI(), factoryClass);
	}

	/**
	 * Remove the working copy of a URI.
	 * 
	 * @param uri
	 * @param factoryClass
	 * @return
	 */
	public synchronized static <WCF extends WorkingCopyFactory> WorkingCopy removeWorkingCopy(URI uri, Class<WCF> factoryClass) {
		checkURI(uri);

		WorkingCopyFactory factory = getWorkingCopyFactory(uri, factoryClass);
		if (factory != null && factory.hasWorkingCopy(uri)) {
			return factory.removeWorkingCopy(uri);
		}
		return null;
	}

	/**
	 * Get the working copy factory of a URI.
	 * 
	 * @param file
	 * @return
	 */
	protected static WorkingCopyFactory getWorkingCopyFactory(URI uri) {
		checkURI(uri);

		WorkingCopyFactory factory = null;
		ResourceFactory<?>[] factories = ResourceFactoryRegistry.INSTANCE.getFactories();
		if (factories != null) {
			for (ResourceFactory<?> currFactory : factories) {
				if (currFactory instanceof WorkingCopyFactory && currFactory.isSupported(uri)) {
					factory = (WorkingCopyFactory) currFactory;
					break;
				}
			}
		}
		return factory;
	}

	/**
	 * Get the working copy factory of a URI.
	 * 
	 * @param uri
	 * @param factoryClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static <WCF extends WorkingCopyFactory> WCF getWorkingCopyFactory(URI uri, Class<WCF> factoryClass) {
		checkURI(uri);

		WCF factory = null;
		ResourceFactory<?>[] factories = ResourceFactoryRegistry.INSTANCE.getFactories();
		if (factories != null) {
			for (ResourceFactory<?> currFactory : factories) {
				if (currFactory instanceof WorkingCopyFactory && currFactory.isSupported(uri) && factoryClass.isAssignableFrom(currFactory.getClass())) {
					factory = (WCF) currFactory;
					break;
				}
			}
		}
		return factory;
	}

	/**
	 * Check file.
	 * 
	 * @param file
	 */
	protected static void checkFile(File file) {
		if (file == null) {
			throw new RuntimeException("file is null.");
		}
	}

	/**
	 * Check URI.
	 * 
	 * @param uri
	 */
	protected static void checkURI(URI uri) {
		if (uri == null) {
			throw new RuntimeException("uri is null.");
		}
	}

}

// /**
// * Get the working copy factory of a file.
// *
// * @param file
// * @param factoryClass
// * @return
// */
// protected static <WCF extends WorkingCopyFactory> WCF getWorkingCopyFactory(File file, Class<WCF> factoryClass) {
// checkFile(file);
// return getWorkingCopyFactory(file.toURI(), factoryClass);
// }

/// **
// * Get the working copy factory of a file.
// *
// * @param file
// * @return
// */
// protected static WorkingCopyFactory getWorkingCopyFactory(File file) {
// checkFile(file);
// return getWorkingCopyFactory(file.toURI());
// }

// /**
// * Get root element from the cache of a file.
// *
// * @param file
// * @param elementClass
// * @return
// */
// @SuppressWarnings("unchecked")
// public synchronized static <ELEMENT> ELEMENT getRootElement(File file, Class<ELEMENT> elementClass) {
// checkFile(file);
//
// ELEMENT result = null;
// WorkingCopy<?> workingCopy = (WorkingCopy<?>) getWorkingCopy(file);
// if (workingCopy != null) {
// try {
// Object rootElement = workingCopy.getRootElement();
// if (rootElement != null && elementClass.isAssignableFrom(rootElement.getClass())) {
// result = (ELEMENT) rootElement;
// }
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// return result;
// }
//
// /**
// * Get root element from the cache of a file.
// *
// * @param file
// * @param clazz
// * @return
// */
// @SuppressWarnings("unchecked")
// public synchronized static <WCF extends WorkingCopyFactory<?>, WC extends WorkingCopy<?>, ELEMENT> ELEMENT getRootElement(File file, Class<WCF>
// factoryClass, Class<WC> wcClass, Class<ELEMENT> clazz) {
// checkFile(file);
//
// ELEMENT result = null;
// WorkingCopy<?> workingCopy = (WorkingCopy<?>) getWorkingCopy(file, factoryClass, wcClass);
// if (workingCopy != null) {
// try {
// Object rootElement = workingCopy.getRootElement();
// if (rootElement != null && clazz.isAssignableFrom(rootElement.getClass())) {
// result = (ELEMENT) rootElement;
// }
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// return result;
// }
