package org.orbit.platform.sdk.extension.desc;

import org.orbit.platform.sdk.condition.ICondition;

public class InterfaceDescription {

	private static final Parameter[] EMPTY_PARAMETERS = new Parameter[0];

	protected String name;
	protected boolean singleton = true;
	protected boolean autoStart = false;
	protected Parameter[] parameters;
	protected ICondition triggerCondition;
	protected Class<?> interfaceClass;
	protected Object interfaceObject;
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
	 * @param name
	 * @param singleton
	 * @param autoStart
	 */
	public InterfaceDescription(String name, boolean singleton, boolean autoStart) {
		this.name = name;
		this.singleton = singleton;
		this.autoStart = autoStart;
	}

	/**
	 * 
	 * @param interfaceClass
	 * @param interfaceObject
	 */
	public InterfaceDescription(Class<?> interfaceClass, Object interfaceObject) {
		this.interfaceClass = interfaceClass;
		this.interfaceObject = interfaceObject;
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

	public Object getInterfaceObject() {
		return this.interfaceObject;
	}

	public void setInterfaceObject(Object interfaceObject) {
		this.interfaceObject = interfaceObject;
	}

	public String getInterfaceClassName() {
		return interfaceClassName;
	}

	public void setInterfaceClassName(String interfaceClassName) {
		this.interfaceClassName = interfaceClassName;
	}

}
