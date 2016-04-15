package org.nb.mgm.runtime.query;

public class HomeQuery {

	public static HomeQueryBuilder newBuilder() {
		return new HomeQueryBuilder();
	}

	protected String name;
	protected String url;
	protected String filter;
	protected String status;

	public HomeQuery() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static class HomeQueryBuilder {
		protected HomeQuery query;

		public HomeQueryBuilder() {
			this.query = new HomeQuery();
		}

		public HomeQuery build() {
			return this.query;
		}

		public HomeQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public HomeQueryBuilder withUrl(String url) {
			this.query.setUrl(url);
			return this;
		}

		public HomeQueryBuilder withFilter(String filter) {
			this.query.setFilter(filter);
			return this;
		}

		public HomeQueryBuilder withStatus(String status) {
			this.query.setStatus(status);
			return this;
		}
	}

}
