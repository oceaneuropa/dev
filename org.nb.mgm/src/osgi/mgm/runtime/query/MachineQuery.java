package osgi.mgm.runtime.query;

public class MachineQuery {

	public static MachineQueryBuilder newBuilder() {
		return new MachineQueryBuilder();
	}

	protected String name;
	protected String ipAddress;
	protected String filter;
	protected String status;

	public MachineQuery() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public static class MachineQueryBuilder {
		protected MachineQuery query;

		public MachineQueryBuilder() {
			this.query = new MachineQuery();
		}

		public MachineQuery build() {
			return this.query;
		}

		public MachineQueryBuilder withName(String name) {
			this.query.setName(name);
			return this;
		}

		public MachineQueryBuilder withIpAddress(String ipAddress) {
			this.query.setIpAddress(ipAddress);
			return this;
		}

		public MachineQueryBuilder withFilter(String filter) {
			this.query.setFilter(filter);
			return this;
		}

		public MachineQueryBuilder withStatus(String status) {
			this.query.setStatus(status);
			return this;
		}
	}

}
