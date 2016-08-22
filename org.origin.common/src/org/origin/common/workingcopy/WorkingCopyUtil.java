package org.origin.common.workingcopy;

import java.io.File;

import org.origin.common.resource.ResourceFactory;
import org.origin.common.resource.ResourceFactoryRegistry;

public class WorkingCopyUtil {

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

	/**
	 * Check whether there is a working copy for a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static boolean hasWorkingCopy(File file) {
		checkFile(file);

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file);
		if (factory != null && factory.hasWorkingCopy(file)) {
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
	public synchronized static <WCF extends WorkingCopyFactory<?>> boolean hasWorkingCopy(File file, Class<WCF> factoryClass) {
		checkFile(file);

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file, factoryClass);
		if (factory != null && factory.hasWorkingCopy(file)) {
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
	public synchronized static WorkingCopy<?> getWorkingCopy(File file) {
		checkFile(file);

		WorkingCopy<?> workingCopy = null;

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file);
		if (factory != null) {
			workingCopy = factory.getWorkingCopy(file);
		}
		return workingCopy;
	}

	/**
	 * Get the working copy of a file.
	 * 
	 * @param file
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <WCF extends WorkingCopyFactory<?>, WC extends WorkingCopy<?>> WC getWorkingCopy(File file, Class<WCF> factoryClass, Class<WC> wcClass) {
		checkFile(file);

		WorkingCopy<?> workingCopy = null;

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file, factoryClass);
		if (factory != null) {
			WorkingCopy<?> currWorkingCopy = factory.getWorkingCopy(file);
			if (currWorkingCopy != null && wcClass.isAssignableFrom(currWorkingCopy.getClass())) {
				workingCopy = currWorkingCopy;
			}
		}
		return (WC) workingCopy;
	}

	/**
	 * Remove the cache of a file.
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static WorkingCopy<?> removeWorkingCopy(File file) {
		checkFile(file);

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file);
		if (factory != null && factory.hasWorkingCopy(file)) {
			return factory.removeWorkingCopy(file);
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
	public synchronized static <WCF extends WorkingCopyFactory<?>> WorkingCopy<?> removeWorkingCopy(File file, Class<WCF> factoryClass) {
		checkFile(file);

		WorkingCopyFactory<?> factory = getWorkingCopyFactory(file, factoryClass);
		if (factory != null && factory.hasWorkingCopy(file)) {
			return factory.removeWorkingCopy(file);
		}
		return null;
	}

	/**
	 * Get the working copy factory of a file.
	 * 
	 * @param file
	 * @return
	 */
	protected static WorkingCopyFactory<?> getWorkingCopyFactory(File file) {
		checkFile(file);

		WorkingCopyFactory<?> factory = null;
		ResourceFactory<?>[] factories = ResourceFactoryRegistry.INSTANCE.getFactories();
		if (factories != null) {
			for (ResourceFactory<?> currFactory : factories) {
				if (currFactory instanceof WorkingCopyFactory && currFactory.isSupported(file)) {
					factory = (WorkingCopyFactory<?>) currFactory;
					break;
				}
			}
		}
		return factory;
	}

	/**
	 * Get the working copy factory of a file.
	 * 
	 * @param file
	 * @param factoryClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static <WCF extends WorkingCopyFactory<?>> WCF getWorkingCopyFactory(File file, Class<WCF> factoryClass) {
		checkFile(file);

		WCF factory = null;
		ResourceFactory<?>[] factories = ResourceFactoryRegistry.INSTANCE.getFactories();
		if (factories != null) {
			for (ResourceFactory<?> currFactory : factories) {
				if (currFactory instanceof WorkingCopyFactory && currFactory.isSupported(file) && factoryClass.isAssignableFrom(currFactory.getClass())) {
					factory = (WCF) currFactory;
					break;
				}
			}
		}
		return factory;
	}

	/**
	 * 
	 * @param file
	 */
	protected static void checkFile(File file) {
		if (file == null) {
			throw new IllegalArgumentException("file is null.");
		}
	}

}
