package org.origin.mgm.model.query;

import java.util.HashMap;
import java.util.Map;

public class IndexItemQuery {

	public static IndexItemQueryBuilder newBuilder() {
		return new IndexItemQueryBuilder();
	}

	protected String type;
	protected String name;
	protected Map<String, Object> properties = new HashMap<String, Object>();

	public IndexItemQuery() {
	}

	public String getType() {
		return type;
	}

	public void setType(String namespace) {
		this.type = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String propName, Object propValue) {
		if (propName == null || propName.trim().isEmpty()) {
			throw new IllegalArgumentException("propName is null.");
		}
		if (propValue != null) {
			properties.put(propName, propValue);
		} else {
			properties.remove(propName);
		}
	}

	public static class IndexItemQueryBuilder {
		protected IndexItemQuery query;

		public IndexItemQueryBuilder() {
			this.query = new IndexItemQuery();
		}

		public IndexItemQuery build() {
			return this.query;
		}

		public IndexItemQueryBuilder withType(String type) {
			this.query.setType(type);
			return this;
		}

		public IndexItemQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public IndexItemQueryBuilder withProperty(String propName, Object propValue) {
			this.query.setProperty(propName, propValue);
			return this;
		}
	}

}
