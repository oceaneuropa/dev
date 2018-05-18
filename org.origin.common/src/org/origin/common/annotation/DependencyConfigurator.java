package org.origin.common.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyConfigurator {

	protected static Logger LOG = LoggerFactory.getLogger(DependencyConfigurator.class);

	protected BundleContext bundleContext;
	protected ServiceTracker<Annotated, Annotated> annotatedTracker;

	protected Set<Annotated> annotatedObjects = Collections.synchronizedSet(new LinkedHashSet<Annotated>());
	protected Map<Annotated, Map<Field, Class>> annotatedToDependencyFieldsMap = Collections.synchronizedMap(new HashMap<Annotated, Map<Field, Class>>());
	protected Map<Class, ServiceTracker> dependentClassToServiceTrackerMap = Collections.synchronizedMap(new HashMap<Class, ServiceTracker>());
	protected Map<Annotated, Boolean> annotatedToDependencyStatusMap = Collections.synchronizedMap(new HashMap<Annotated, Boolean>());

	public DependencyConfigurator(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void start() {
		// System.out.println("DependencyConfigurator.start()");
		try {
			Collection<ServiceReference<Annotated>> serviceReferences = bundleContext.getServiceReferences(Annotated.class, null);
			for (ServiceReference<Annotated> serviceReference : serviceReferences) {
				Annotated annotated = bundleContext.getService(serviceReference);
				long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);

				LOG.debug("Annotated service [" + annotated.getClass().getName() + "] (service.id='" + service_id + "') is added.");

				addAnnotated(annotated);
				bundleContext.ungetService(serviceReference);
			}
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}

		// Track the adding or removing of any Annotated service.
		annotatedTracker = new ServiceTracker<Annotated, Annotated>(bundleContext, Annotated.class.getName(), null) {
			@Override
			public Annotated addingService(ServiceReference<Annotated> serviceReference) {
				Annotated annotated = super.addingService(serviceReference);
				long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);

				LOG.debug("Annotated service [" + annotated.getClass().getName() + "] (service.id='" + service_id + "') is added.");

				addAnnotated(annotated);
				return annotated;
			}

			@Override
			public void removedService(ServiceReference<Annotated> serviceReference, Annotated annotated) {
				long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);

				LOG.debug("Annotated service [" + annotated.getClass().getName() + "] (service.id='" + service_id + "') is removed.");

				removeAnnotated(annotated);
				super.removedService(serviceReference, annotated);
			}
		};
		annotatedTracker.open();
	}

	public void stop() {
		// System.out.println("DependencyConfigurator.stop()");
		// Stops tracking annotated services.
		if (this.annotatedTracker != null) {
			this.annotatedTracker.close();
			this.annotatedTracker = null;
		}

		// Closing existing ServiceTracker for dependent services.
		for (Iterator<Class> dependentClassItor = dependentClassToServiceTrackerMap.keySet().iterator(); dependentClassItor.hasNext();) {
			Class dependentClass = dependentClassItor.next();
			disposeDependentServiceTracker(dependentClass);
		}
		this.dependentClassToServiceTrackerMap.clear();

		// Clear the map of tracked annotated object and its dependency fields.
		this.annotatedToDependencyFieldsMap.clear();

		// Clear the map of tracked annotated object and its dependency status.
		this.annotatedToDependencyStatusMap.clear();

		// Clear the list of tracked annotated services
		this.annotatedObjects.clear();
	}

	/**
	 * Called when a Annotated object is added.
	 * 
	 * @param annotated
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addAnnotated(Annotated annotated) {
		// System.out.println("DependencyConfigurator.addAnnotated() " + annotated.getClass().getSimpleName());

		if (annotated != null && !annotatedObjects.contains(annotated)) {
			annotatedObjects.add(annotated);

			Map<Field, Class> dependencyFieldsMap = getDependencyFieldMap(annotated);
			if (dependencyFieldsMap != null && !dependencyFieldsMap.isEmpty()) {
				annotatedToDependencyFieldsMap.put(annotated, dependencyFieldsMap);

				boolean ignore = true;
				if (!ignore) {
					// For each Field with dependency. Get the dependent class and get the existing dependent service (through ServiceReferences) for
					// it. If a dependent service already exists, add it to the Field.
					for (Iterator<Field> fieldItor = dependencyFieldsMap.keySet().iterator(); fieldItor.hasNext();) {
						Field field = fieldItor.next();
						Class dependentClass = dependencyFieldsMap.get(field);

						Object dependentService = null;
						try {
							Collection<ServiceReference<?>> serviceReferences = bundleContext.getServiceReferences(dependentClass, null);
							for (ServiceReference<?> serviceReference : serviceReferences) {
								try {
									dependentService = bundleContext.getService(serviceReference);
									if (dependentService != null) {
										long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
										// System.out.println("Service for " + dependentClass.getName() + " (service.id='" + service_id + "') already exists.");
										LOG.debug("Annotated service [" + annotated.getClass().getName() + "] (service.id='" + service_id + "') is already exists.");

										break;
									}
								} finally {
									bundleContext.ungetService(serviceReference);
								}
							}
							if (dependentService != null) {
								// Set dependentService to the field of the Annotated object.
								dependentServiceAdded(annotated, field, dependentService);
							}
						} catch (InvalidSyntaxException e) {
							e.printStackTrace();
						}
					}

					// Evaluate the dependency status of the current Annotated object
					evaluateDependendy(annotated);
				}

				for (Iterator<Field> fieldItor = dependencyFieldsMap.keySet().iterator(); fieldItor.hasNext();) {
					Field field = fieldItor.next();
					Class dependentClass = dependencyFieldsMap.get(field);

					Object dependentService = null;

					// init ServiceTracker for the dependentClasses
					ServiceTracker<Object, Object> dependentServiceTracker = initDependentServiceTracker(dependentClass);
					if (dependentServiceTracker != null) {
						SortedMap<ServiceReference<Object>, Object> map = dependentServiceTracker.getTracked();
						if (map != null) {
							for (Iterator<Object> dependentServiceItor = map.values().iterator(); dependentServiceItor.hasNext();) {
								dependentService = dependentServiceItor.next();
								if (dependentService != null) {
									break;
								}
							}
						}
					}

					if (dependentService != null) {
						dependentServiceAdded(annotated, field, dependentService);
					}
				}

				// Evaluate the dependency status of the current Annotated object
				evaluateDependendy(annotated);
			}
		}
	}

	/**
	 * Called when a Annotated object is removed.
	 * 
	 * @param annotated
	 */
	public void removeAnnotated(Annotated annotated) {
		// System.out.println("DependencyConfigurator.removeAnnotated() " + annotated.getClass().getSimpleName());

		if (annotated != null && annotatedObjects.contains(annotated)) {
			annotatedObjects.remove(annotated);

			Map<Field, Class> dependencyFieldsMap = annotatedToDependencyFieldsMap.remove(annotated);
			if (dependencyFieldsMap != null && !dependencyFieldsMap.isEmpty()) {
				// Collection<Class> dependentClasses = dependencyFieldsMap.values();
				// for (Iterator<Class> classItor = dependentClasses.iterator(); classItor.hasNext();) {
				// Class dependentClass = classItor.next();
				//
				// boolean beingUsed = false;
				// for (Iterator<Annotated> annotatedItor = annotatedToDependencyFieldsMap.keySet().iterator(); annotatedItor.hasNext();) {
				// Annotated currAnnotated = annotatedItor.next();
				// if (annotated.equals(currAnnotated)) {
				// continue;
				// }
				// Map<Field, Class> currDependencyFieldsMap = annotatedToDependencyFieldsMap.get(currAnnotated);
				// if (currDependencyFieldsMap != null && currDependencyFieldsMap.values().contains(dependentClass)) {
				// beingUsed = true;
				// break;
				// }
				// }
				// if (!beingUsed) {
				// disposeDependentServiceTracker(dependentClass);
				// }
				// }
				// annotatedToDependencyFieldsMap.remove(annotated);
			}
		}
	}

	/**
	 * Get a map of Field and its dependent class from a annotated object.
	 * 
	 * @param annotated
	 * @return
	 */
	protected Map<Field, Class> getDependencyFieldMap(Object annotated) {
		Map<Field, Class> fieldToDependentClassMap = new HashMap<Field, Class>();
		Field[] fields = annotated.getClass().getDeclaredFields();
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (Dependency.class.equals(annotation.annotationType())) {
					Class dependentClass = ((Dependency) annotation).type();
					if (Object.class.equals(dependentClass)) {
						fieldToDependentClassMap.put(field, field.getType());
					} else {
						fieldToDependentClassMap.put(field, dependentClass);
					}
				}
			}
		}
		return fieldToDependentClassMap;
	}

	/**
	 * Create a ServiceTracker for a dependent class, if not exist, to track the dependent service.
	 * 
	 * @param dependentClass
	 */
	@SuppressWarnings("unchecked")
	protected <T> ServiceTracker<T, T> initDependentServiceTracker(final Class<T> dependentClass) {
		// System.out.println("DependencyConfigurator.initDependentServiceTracker() for " + dependentClass.getName());

		ServiceTracker<T, T> serviceTracker = dependentClassToServiceTrackerMap.get(dependentClass);
		if (serviceTracker != null) {
			// ServiceTracker for a dependent class has been opened.
			return serviceTracker;
		}

		serviceTracker = new ServiceTracker<T, T>(bundleContext, dependentClass.getName(), null) {
			@Override
			public T addingService(ServiceReference<T> serviceReference) {
				T dependentService = super.addingService(serviceReference);
				try {
					long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					// System.out.println("DependencyConfigurator.initDependentServiceTracker() " + dependentClass.getName() + " (service.id='" + service_id +
					// "') is added.");

					for (Iterator<Annotated> annotatedItor = annotatedToDependencyFieldsMap.keySet().iterator(); annotatedItor.hasNext();) {
						Annotated annotated = annotatedItor.next();
						Map<Field, Class> dependencyFieldsMap = annotatedToDependencyFieldsMap.get(annotated);

						if (dependencyFieldsMap == null || dependencyFieldsMap.isEmpty() || !dependencyFieldsMap.values().contains(dependentClass)) {
							continue;
						}

						for (Iterator<Field> fieldItor = dependencyFieldsMap.keySet().iterator(); fieldItor.hasNext();) {
							Field field = fieldItor.next();
							Class currDependentClass = dependencyFieldsMap.get(field);
							if (dependentClass.equals(currDependentClass)) {
								// Set dependentService to the field of the Annotated object.
								dependentServiceAdded(annotated, field, dependentService);
							}
						}
						// Evaluate the dependency status of the current Annotated object
						evaluateDependendy(annotated);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return dependentService;
			}

			@Override
			public void removedService(ServiceReference<T> serviceReference, T dependentService) {
				try {
					long service_id = (Long) serviceReference.getProperty(Constants.SERVICE_ID);
					// System.out.println("DependencyConfigurator.initDependentServiceTracker() " + dependentClass.getName() + " (service.id='" + service_id +
					// "') is removed.");

					for (Iterator<Annotated> annotatedItor = annotatedToDependencyFieldsMap.keySet().iterator(); annotatedItor.hasNext();) {
						Annotated annotated = annotatedItor.next();
						Map<Field, Class> dependencyFieldsMap = annotatedToDependencyFieldsMap.get(annotated);

						if (dependencyFieldsMap == null || dependencyFieldsMap.isEmpty() || !dependencyFieldsMap.values().contains(dependentClass)) {
							continue;
						}

						for (Iterator<Field> fieldItor = dependencyFieldsMap.keySet().iterator(); fieldItor.hasNext();) {
							Field field = fieldItor.next();
							Class currDependentClass = dependencyFieldsMap.get(field);
							if (dependentClass.equals(currDependentClass)) {
								// Unset dependentService to the field of the Annotated object.
								dependentServiceRemoved(annotated, field, dependentService);
							}
						}
						// Evaluate the dependency status of the current Annotated object
						evaluateDependendy(annotated);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.removedService(serviceReference, dependentService);
			}
		};
		serviceTracker.open();

		dependentClassToServiceTrackerMap.put(dependentClass, serviceTracker);

		return serviceTracker;
	}

	/**
	 * Close the ServiceTracker for a dependent class.
	 * 
	 * @param dependentClass
	 */
	protected <T> void disposeDependentServiceTracker(Class<?> dependentClass) {
		// System.out.println("DependencyConfigurator.disposeDependentServiceTracker() for " + dependentClass.getName());
		if (dependentClassToServiceTrackerMap.containsKey(dependentClass)) {
			ServiceTracker<?, ?> serviceTracker = dependentClassToServiceTrackerMap.remove(dependentClass);
			if (serviceTracker != null) {
				serviceTracker.close();
			}
		}
	}

	/**
	 * Set a dependent service to a Field of a Annotated object. Called when a dependent service is added.
	 * 
	 * @param annotated
	 * @param dependentClass
	 */
	protected <T> void dependentServiceAdded(Annotated annotated, Field field, T dependentService) {
		// System.out.println("DependencyConfigurator.dependentServiceAdded() Set [" + dependentService.getClass() + "] to Field [" +
		// annotated.getClass().getSimpleName() + "." + field.getName() + "].");
		boolean originalIsAccessible = field.isAccessible();
		try {
			if (!originalIsAccessible) {
				field.setAccessible(true);
			}
			field.set(annotated, dependentService);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			if (!originalIsAccessible) {
				field.setAccessible(originalIsAccessible);
			}
		}
	}

	/**
	 * Unset dependent service from a Field of a Annotated object. Called when a dependent service is removed.
	 * 
	 * @param annotated
	 * @param dependentClass
	 */
	protected <T> void dependentServiceRemoved(Annotated annotated, Field field, T dependentService) {
		// System.out.println("DependencyConfigurator.dependentServiceRemoved() Remove [" + dependentService.getClass() + "] from Field [" +
		// annotated.getClass().getSimpleName() + "." + field.getName() + "].");
		boolean originalIsAccessible = field.isAccessible();
		try {
			if (!originalIsAccessible) {
				field.setAccessible(true);
			}
			field.set(annotated, null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			if (!originalIsAccessible) {
				field.setAccessible(originalIsAccessible);
			}
		}
	}

	/**
	 * Evaluate the dependency status of a Annotated object
	 * 
	 * @param annotated
	 */
	protected void evaluateDependendy(Annotated annotated) {
		// System.out.println("DependencyConfigurator.evaluateDependendy() for Annotated [" + annotated.getClass().getName() + "].");

		Map<Field, Class> dependencyFieldsMap = annotatedToDependencyFieldsMap.get(annotated);
		if (dependencyFieldsMap != null && !dependencyFieldsMap.isEmpty()) {

			boolean allDependencyResolved = false;
			for (Iterator<Field> fieldItor = dependencyFieldsMap.keySet().iterator(); fieldItor.hasNext();) {
				// a Field with @Dependency annotation
				Field field = fieldItor.next();
				boolean originalIsAccessible = field.isAccessible();
				Object value = null;
				try {
					if (!originalIsAccessible) {
						field.setAccessible(true);
					}
					value = field.get(annotated);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				} finally {
					if (!originalIsAccessible) {
						field.setAccessible(originalIsAccessible);
					}
				}
				if (value != null) {
					allDependencyResolved = true;
				} else {
					allDependencyResolved = false;
					break;
				}
			}

			Boolean existingAllDependencyResolved = annotatedToDependencyStatusMap.get(annotated);
			if (existingAllDependencyResolved == null) {
				existingAllDependencyResolved = Boolean.FALSE;
				annotatedToDependencyStatusMap.put(annotated, existingAllDependencyResolved);
			}

			if (allDependencyResolved) {
				// System.out.println("All dependencies have been resolved for Annotated [" + annotated.getClass().getName() + "].");
				// All dependencies have been resolved.
				if (!existingAllDependencyResolved) {
					// Call dependencyFullfilled callback methods when existing status is not resolved.
					Method[] dependencyFullfilledMethods = AnnotationUtil.getAnnotationMethods(annotated, DependencyFullfilled.class);
					for (Method dependencyFullfilledMethod : dependencyFullfilledMethods) {
						try {
							// Method is expected to have no parameters.
							dependencyFullfilledMethod.invoke(annotated, null);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}

			} else {
				// System.out.println("Not all dependencies have been resolved for Annotated [" + annotated.getClass().getName() + "].");
				// Not all dependencies have been resolved.
				if (existingAllDependencyResolved) {
					// Call dependencyUnfullfilled callback methods when existing status is resolved.
					Method[] dependencyUnfullfilledMethods = AnnotationUtil.getAnnotationMethods(annotated, DependencyUnfullfilled.class);
					for (Method dependencyUnfullfilledMethod : dependencyUnfullfilledMethods) {
						try {
							// Method is expected to have no parameters.
							dependencyUnfullfilledMethod.invoke(annotated, null);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// Update dependency fullfillment status for the Annotated object.
			annotatedToDependencyStatusMap.put(annotated, allDependencyResolved);
		}
	}

}
