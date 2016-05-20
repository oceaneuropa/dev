package org.origin.common.osgi;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class OSGiServiceUtil {

	protected static Map<Object, List<ServiceRegistration<?>>> serviceToRegistrationMap = new HashMap<Object, List<ServiceRegistration<?>>>();
	protected static ReadWriteLock rwLock = new ReentrantReadWriteLock();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				dispose();
			}
		}, "shutdown-hook-thread"));
	}

	private static void checkBundleContext(BundleContext bundleContext) {
		if (bundleContext == null) {
			throw new IllegalArgumentException("bundleContext is null.");
		}
	}

	private static void checkService(Object service) {
		if (service == null) {
			throw new IllegalArgumentException("service is null.");
		}
	}

	private static void checkClassNames(String[] classNames) {
		if (classNames == null) {
			throw new IllegalArgumentException("classNames is null.");
		}
		if (classNames.length == 0) {
			throw new IllegalArgumentException("classNames is empty.");
		}
	}

	private static void checkClassName(String className) {
		if (className == null) {
			throw new IllegalArgumentException("className is null.");
		}
	}

	private static void checkClass(Class clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("clazz is null.");
		}
	}

	private static List<ServiceRegistration<?>> getServiceRegistrations(Object service) {
		List<ServiceRegistration<?>> serviceRegistrations = serviceToRegistrationMap.get(service);
		if (serviceRegistrations == null) {
			serviceRegistrations = new ArrayList<ServiceRegistration<?>>();
			serviceToRegistrationMap.put(service, serviceRegistrations);
		}
		return serviceRegistrations;
	}

	/**
	 * 
	 * @param bundleContext
	 * @param classNames
	 * @param service
	 */
	public static void register(BundleContext bundleContext, String[] classNames, Object service) {
		register(bundleContext, classNames, service, null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param classNames
	 * @param service
	 * @param properties
	 */
	public static void register(BundleContext bundleContext, String[] classNames, Object service, Dictionary<String, ?> properties) {
		System.out.println("OSGiServiceUtil.register(" + service + ")");

		checkBundleContext(bundleContext);
		checkClassNames(classNames);
		checkService(service);

		rwLock.writeLock().lock();
		try {
			ServiceRegistration<?> serviceRegistration = bundleContext.registerService(classNames, service, properties);
			if (serviceRegistration != null) {
				getServiceRegistrations(service).add(serviceRegistration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param className
	 * @param service
	 */
	public static void register(BundleContext bundleContext, String className, Object service) {
		register(bundleContext, className, service);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param className
	 * @param service
	 * @param properties
	 */
	public static void register(BundleContext bundleContext, String className, Object service, Dictionary<String, ?> properties) {
		System.out.println("OSGiServiceUtil.register(" + service + ")");

		checkBundleContext(bundleContext);
		checkService(service);
		checkClassName(className);

		rwLock.writeLock().lock();
		try {
			ServiceRegistration<?> serviceRegistration = bundleContext.registerService(className, service, properties);
			if (serviceRegistration != null) {
				getServiceRegistrations(service).add(serviceRegistration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param bundleContext
	 * @param clazz
	 * @param service
	 */
	public static void register(BundleContext bundleContext, Class clazz, Object service) {
		register(bundleContext, clazz, service, null);
	}

	/**
	 * 
	 * @param bundleContext
	 * @param clazz
	 * @param service
	 * @param properties
	 */
	public static void register(BundleContext bundleContext, Class clazz, Object service, Dictionary<String, ?> properties) {
		System.out.println("OSGiServiceUtil.register(" + service + ")");

		checkBundleContext(bundleContext);
		checkService(service);
		checkClass(clazz);

		rwLock.writeLock().lock();
		try {
			ServiceRegistration<?> serviceRegistration = bundleContext.registerService(clazz, service, properties);
			if (serviceRegistration != null) {
				getServiceRegistrations(service).add(serviceRegistration);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

	/**
	 * 
	 * @param service
	 */
	public static void unregister(Object service) {
		System.out.println("OSGiServiceUtil.unregister(" + service + ")");

		if (service == null) {
			// fail without throwing exception
			System.err.println("OSGiServiceUtil.unregister(). service is null.");
			return;
		}

		rwLock.writeLock().lock();
		try {
			List<ServiceRegistration<?>> serviceRegistrations = serviceToRegistrationMap.remove(service);
			if (serviceRegistrations != null) {
				for (ServiceRegistration<?> serviceRegistration : serviceRegistrations) {
					try {
						serviceRegistration.unregister();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} finally {
			rwLock.writeLock().unlock();
			// System.out.println("OSGiServiceRegistry.unregister(" + service + ") is done.");
		}
	}

	protected static void dispose() {
		System.out.println("OSGiServiceUtil.dispose()");
		rwLock.writeLock().lock();
		try {
			if (!serviceToRegistrationMap.isEmpty()) {
				Set<Object> services = serviceToRegistrationMap.keySet();

				// The unregister() method removes the service object from the map.
				// Pass service object from map.keySet() directly to the unregister() method causes concurrent modify exception.
				// That means map.keySet() returns a Set that is still hold by the map.
				// To avoid that exception, add the service objects to another List.
				List<Object> servicesToRemove = new ArrayList<Object>();
				servicesToRemove.addAll(services);

				for (Object serviceToRemove : servicesToRemove) {
					unregister(serviceToRemove);
				}
				serviceToRegistrationMap.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
			// System.out.println("OSGiServiceRegistry.dispose() is done.");
		}
	}

}
