package org.origin.common.extensions;

import org.origin.common.extensions.condition.ICondition;

public class InterfaceDescription {

	private static final Parameter[] EMPTY_PARAMETERS = new Parameter[0];

	protected String name;
	protected boolean singleton = true;
	protected boolean autoStart = false;
	protected Parameter[] parameters;
	protected ICondition triggerCondition;
	protected Class<?> interfaceClass;
	protected Class<?> interfaceImplClass;
	protected Object interfaceInstance;
	protected String interfaceClassName;

	public InterfaceDescription() {
	}

	/**
	 * 
	 * @param name
	 */
	public InterfaceDescription(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param interfaceClass
	 * @param interfaceClassName
	 */
	public InterfaceDescription(Class<?> interfaceClass, String interfaceClassName) {
		this(interfaceClass.getSimpleName(), interfaceClass, interfaceClassName);
	}

	/**
	 * 
	 * @param interfaceClass
	 * @param interfaceImplClass
	 */
	public <I, IMPL extends I> InterfaceDescription(Class<I> interfaceClass, Class<IMPL> interfaceImplClass) {
		this(interfaceImplClass.getSimpleName(), interfaceClass, interfaceImplClass);
	}

	/**
	 * 
	 * @param name
	 * @param interfaceClass
	 * @param interfaceClassName
	 */
	public InterfaceDescription(String name, Class<?> interfaceClass, String interfaceClassName) {
		this.name = name;
		this.interfaceClass = interfaceClass;
		this.interfaceClassName = interfaceClassName;
	}

	/**
	 * 
	 * @param name
	 * @param interfaceClass
	 * @param interfaceImplClass
	 */
	public InterfaceDescription(String name, Class<?> interfaceClass, Class<?> interfaceImplClass) {
		this.name = name;
		this.interfaceClass = interfaceClass;
		this.interfaceImplClass = interfaceImplClass;
	}

	/**
	 * 
	 * @param interfaceClass
	 * @param interfaceInstance
	 */
	public InterfaceDescription(Class<?> interfaceClass, Object interfaceInstance) {
		this.name = interfaceClass.getSimpleName();
		this.interfaceClass = interfaceClass;
		this.interfaceInstance = interfaceInstance;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSingleton() {
		return this.singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isAutoStart() {
		return this.autoStart;
	}

	public void setAutoStart(boolean autoStart) {
		this.autoStart = autoStart;
	}

	public synchronized Parameter[] getParameters() {
		if (this.parameters == null) {
			this.parameters = EMPTY_PARAMETERS;
		}
		return this.parameters;
	}

	public synchronized void setParameters(Parameter... parameters) {
		this.parameters = parameters;
	}

	public ICondition getTriggerCondition() {
		return this.triggerCondition;
	}

	public void setTriggerCondition(ICondition triggerCondition) {
		this.triggerCondition = triggerCondition;
	}

	public Class<?> getInterfaceClass() {
		return this.interfaceClass;
	}

	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public Class<?> getInterfaceImplClass() {
		return this.interfaceImplClass;
	}

	public void setInterfaceImplClass(Class<?> interfaceImplClass) {
		this.interfaceImplClass = interfaceImplClass;
	}

	public Object getInterfaceInstance() {
		return this.interfaceInstance;
	}

	public void setInterfaceInstance(Object interfaceInstance) {
		this.interfaceInstance = interfaceInstance;
	}

	public String getInterfaceClassName() {
		return interfaceClassName;
	}

	public void setInterfaceClassName(String interfaceClassName) {
		this.interfaceClassName = interfaceClassName;
	}

}
