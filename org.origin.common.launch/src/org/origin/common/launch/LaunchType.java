package org.origin.common.launch;

public interface LaunchType {

	public static final String TYPE_ID = "org.origin.common.launch.LaunchType";

	public static enum Types {
		NODE("node", "Node"); //$NON-NLS-1$ //$NON-NLS-2$

		protected String id;
		protected String name;

		/**
		 * 
		 * @param id
		 * @param name
		 */
		Types(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return this.id;
		}

		public String getName() {
			return this.name;
		}
	}

	String getId();

	String getName();

}

// public static final String PROP_LAUNCHER_IDS = "LAUNCHER_IDS";
// String[] getLauncherIds();
