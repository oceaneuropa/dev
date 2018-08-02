package org.orbit.component.runtime.model.appstore;

/**
 * Runtime object for App category.
 *
 */
public class AppCategory {

	protected String categoryId;
	protected String parentId;
	protected String name;
	protected String description;

	public AppCategory() {
	}

	/**
	 * 
	 * @param categoryId
	 * @param parentId
	 * @param name
	 * @param description
	 */
	public AppCategory(String categoryId, String parentId, String name, String description) {
		assert (categoryId != null) : "categoryId is null";
		assert (parentId != null) : "parentId is null";
		assert (name != null) : "name is null";

		this.categoryId = categoryId;
		this.parentId = parentId;
		this.name = name;
		this.description = description;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AppCategoryRTO(");
		sb.append("categoryId=").append(this.categoryId);
		sb.append(", parentId=").append(this.parentId);
		sb.append(", name=").append(this.name);
		sb.append(", description=").append(this.description);
		sb.append(")");
		return sb.toString();
	}

}
