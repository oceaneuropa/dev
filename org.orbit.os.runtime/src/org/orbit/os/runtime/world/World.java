package org.orbit.os.runtime.world;

public class World {

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

}
