package org.nb.common.rest.model;

public class ModelObject {

	protected Object parent;
	protected String id;
	protected String name;
	protected String description;

	public ModelObject() {
	}

	/**
	 * 
	 * @param parent
	 */
	public ModelObject(Object parent) {
		this.parent = parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public Object getParent() {
		return this.parent;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getParent(Class<T> clazz) {
		T result = null;
		if (clazz != null) {
			Object currParent = this.parent;
			while (currParent != null) {
				if (clazz.isAssignableFrom(currParent.getClass())) {
					result = (T) currParent;
					break;
				}
				if (currParent instanceof ModelObject) {
					currParent = ((ModelObject) currParent).getParent();
				}
			}
		}
		return result;
	}

	public <T> T getRoot(Class<T> clazz) {
		return getParent(clazz);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
