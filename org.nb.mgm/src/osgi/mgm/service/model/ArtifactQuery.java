package osgi.mgm.service.model;

public class ArtifactQuery {

	public static ArtifactQueryBuilder newBuilder() {
		return new ArtifactQueryBuilder();
	}

	protected String name;
	protected String version;
	protected String fileName;
	protected String filePath;
	protected String filter;

	public ArtifactQuery() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public static class ArtifactQueryBuilder {
		protected ArtifactQuery query;

		public ArtifactQueryBuilder() {
			this.query = new ArtifactQuery();
		}

		public ArtifactQuery build() {
			return this.query;
		}

		public ArtifactQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public ArtifactQueryBuilder withFilter(String filter) {
			this.query.setFilter(filter);
			return this;
		}
	}

}
