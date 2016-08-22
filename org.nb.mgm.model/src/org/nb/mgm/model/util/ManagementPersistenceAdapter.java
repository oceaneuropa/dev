package org.nb.mgm.model.util;

import org.nb.mgm.model.runtime.ClusterRoot;

/**
 * Cluster data model persistence adapter.
 * 
 */
public interface ManagementPersistenceAdapter {

	/** persistence type constant. */
	public static final String PERSISTENCE_TYPE = "cluster.persistence.type";
	/** persistence type values: db or local. */
	public static final String PERSISTENCE_TYPE_DB = "db";
	public static final String PERSISTENCE_TYPE_LOCAL = "local";

	/** persistence local dir */
	public static final String PERSISTENCE_LOCAL_DIR = "cluster.persistence.local.dir";

	/** persistence autosave */
	public static final String PERSISTENCE_AUTOSAVE = "cluster.persistence.autosave";

	/**
	 * Load cluster root data model.
	 * 
	 * @return
	 */
	public ClusterRoot load();

	/**
	 * Save cluster root data model.
	 * 
	 * @param root
	 */
	public void save(ClusterRoot root);

}
