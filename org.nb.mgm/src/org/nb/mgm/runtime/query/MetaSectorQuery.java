package org.nb.mgm.runtime.query;

public class MetaSectorQuery {

	public static MetaSectorQueryBuilder newBuilder() {
		return new MetaSectorQueryBuilder();
	}

	protected String name;
	protected String filter;

	public MetaSectorQuery() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public static class MetaSectorQueryBuilder {
		protected MetaSectorQuery query;

		public MetaSectorQueryBuilder() {
			this.query = new MetaSectorQuery();
		}

		public MetaSectorQuery build() {
			return this.query;
		}

		public MetaSectorQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public MetaSectorQueryBuilder withFilter(String filter) {
			this.query.setFilter(filter);
			return this;
		}
	}

}
