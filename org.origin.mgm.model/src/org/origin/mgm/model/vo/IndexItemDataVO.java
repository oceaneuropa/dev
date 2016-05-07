package org.origin.mgm.model.vo;

public class IndexItemDataVO {

	protected Integer id;
	protected String type;
	protected String name;
	protected String propertiesString;
	protected String lastUpdateTimeString;

	public IndexItemDataVO() {
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @param name
	 * @param propertiesString
	 * @param lastUpdateTimeString
	 */
	public IndexItemDataVO(Integer id, String type, String name, String propertiesString, String lastUpdateTimeString) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.propertiesString = propertiesString;
		this.lastUpdateTimeString = lastUpdateTimeString;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertiesString() {
		return propertiesString;
	}

	public void setPropertiesString(String propertiesString) {
		this.propertiesString = propertiesString;
	}

	public String getLastUpdateTimeString() {
		return lastUpdateTimeString;
	}

	public void setLastUpdateTimeString(String lastUpdateTimeString) {
		this.lastUpdateTimeString = lastUpdateTimeString;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IndexItemDataVO(");
		sb.append("id=").append(this.id);
		sb.append(", type=").append(this.type);
		sb.append(", name=").append(this.name);
		sb.append(", propertiesString=").append(this.propertiesString);
		sb.append(", lastUpdateTimeString=").append(this.lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
