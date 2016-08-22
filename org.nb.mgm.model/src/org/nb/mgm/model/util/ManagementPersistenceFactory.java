package org.nb.mgm.model.util;

import static org.nb.mgm.model.util.ManagementPersistenceAdapter.PERSISTENCE_TYPE;
import static org.nb.mgm.model.util.ManagementPersistenceAdapter.PERSISTENCE_TYPE_DB;
import static org.nb.mgm.model.util.ManagementPersistenceAdapter.PERSISTENCE_TYPE_LOCAL;

import java.util.Map;

/**
 * Persistence adapter factory.
 * 
 */
public class ManagementPersistenceFactory {

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static ManagementPersistenceAdapter newInstance(Map<Object, Object> props) {
		ManagementPersistenceAdapter persistenceAdapter = null;
		if (props != null && props.get(PERSISTENCE_TYPE) instanceof String) {
			String type = (String) props.get(PERSISTENCE_TYPE);

			if (PERSISTENCE_TYPE_DB.equals(type)) {
				persistenceAdapter = new ManagementDatabasePersistenceAdapter();
			} else if (PERSISTENCE_TYPE_LOCAL.equals(type)) {
				persistenceAdapter = new ManagementLocalPersistenceAdapter(props);
			}
		}

		if (persistenceAdapter == null) {
			persistenceAdapter = new ManagementLocalPersistenceAdapter(props);
		}
		return persistenceAdapter;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	public String getPersistenceType(Map<Object, Object> props) {
		String persistenceType = null;
		if (props.get(ManagementPersistenceAdapter.PERSISTENCE_TYPE) instanceof String) {
			persistenceType = (String) props.get(ManagementPersistenceAdapter.PERSISTENCE_TYPE);
		}
		return persistenceType;
	}

	/**
	 * 
	 * @param props
	 * @return
	 */
	public String getLocalDirPath(Map<Object, Object> props) {
		String localDirPath = null;
		if (props.get(ManagementPersistenceAdapter.PERSISTENCE_LOCAL_DIR) instanceof String) {
			localDirPath = (String) props.get(ManagementPersistenceAdapter.PERSISTENCE_LOCAL_DIR);
		}
		return localDirPath;
	}

}
