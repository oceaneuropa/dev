package osgi.mgm.runtime.query;

public class MetaSpaceQuery {

	public static MetaSpaceQueryBuilder newBuilder() {
		return new MetaSpaceQueryBuilder();
	}

	protected String name;
	protected String filter;

	public MetaSpaceQuery() {
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

	public static class MetaSpaceQueryBuilder {
		protected MetaSpaceQuery query;

		public MetaSpaceQueryBuilder() {
			this.query = new MetaSpaceQuery();
		}

		public MetaSpaceQuery build() {
			return this.query;
		}

		public MetaSpaceQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public MetaSpaceQueryBuilder withFilter(String filter) {
			this.query.setFilter(filter);
			return this;
		}
	}

}
