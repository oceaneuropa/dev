package org.origin.mgm.model.vo;

public class IndexItemDataVO {

	protected Integer indexItemId;
	protected String indexProviderId;
	protected String namespace;
	protected String name;
	protected String propertiesString;
	protected String createTimeString;
	protected String lastUpdateTimeString;

	public IndexItemDataVO() {
	}

	/**
	 * 
	 * @param indexItemId
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 * @param propertiesString
	 * @param createTimeString
	 * @param lastUpdateTimeString
	 */
	public IndexItemDataVO(Integer indexItemId, String indexProviderId, String namespace, String name, String propertiesString, String createTimeString, String lastUpdateTimeString) {
		this.indexItemId = indexItemId;
		this.indexProviderId = indexProviderId;
		this.namespace = namespace;
		this.name = name;
		this.propertiesString = propertiesString;
		this.createTimeString = createTimeString;
		this.lastUpdateTimeString = lastUpdateTimeString;
	}

	public Integer getIndexItemId() {
		return indexItemId;
	}

	public void setIndexItemId(Integer indexItemId) {
		this.indexItemId = indexItemId;
	}

	public String getIndexProviderId() {
		return indexProviderId;
	}

	public void setIndexProviderId(String indexProviderId) {
		this.indexProviderId = indexProviderId;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	public String getCreateTimeString() {
		return createTimeString;
	}

	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
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
		sb.append("indexItemId=").append(this.indexItemId);
		sb.append("indexProviderId=").append(this.indexProviderId);
		sb.append(", namespace=").append(this.namespace);
		sb.append(", name=").append(this.name);
		sb.append(", propertiesString=").append(this.propertiesString);
		sb.append(", createTimeString=").append(this.createTimeString);
		sb.append(", lastUpdateTimeString=").append(this.lastUpdateTimeString);
		sb.append(")");
		return sb.toString();
	}

}
