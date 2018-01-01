package org.spirit.world.api;

public interface World {

	public enum RUNTIME_STATE {
		STARTING("STARTING"), //
		STARTED("STARTED"), //
		STOPPING("STOPPING"), //
		STOPPED("STOPPED"); //

		protected String state;

		RUNTIME_STATE(String state) {
			this.state = state;
		}

		public boolean isStarting() {
			return ("STARTING").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStarted() {
			return ("STARTED").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopping() {
			return ("STOPPING").equalsIgnoreCase(this.state) ? true : false;
		}

		public boolean isStopped() {
			return ("STOPPED").equalsIgnoreCase(this.state) ? true : false;
		}
	}

	RUNTIME_STATE getRuntimeState();

	void setName(String name);

	String getName();

	void setInput(WorldInput input);

	WorldInput getInput();

	void start();

	void shutdown();

}
