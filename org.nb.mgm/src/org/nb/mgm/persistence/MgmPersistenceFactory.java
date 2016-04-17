package org.nb.mgm.persistence;

import static org.nb.mgm.persistence.MgmPersistenceAdapter.PERSISTENCE_TYPE;
import static org.nb.mgm.persistence.MgmPersistenceAdapter.PERSISTENCE_TYPE_DB;
import static org.nb.mgm.persistence.MgmPersistenceAdapter.PERSISTENCE_TYPE_LOCAL;

import java.util.Map;

/**
 * Persistence adapter factory.
 * 
 */
public class MgmPersistenceFactory {

	/**
	 * 
	 * @param props
	 * @return
	 */
	public static MgmPersistenceAdapter createInstance(Map<String, Object> props) {
		MgmPersistenceAdapter persistenceAdapter = null;
		if (props != null && props.get(PERSISTENCE_TYPE) instanceof String) {
			String type = (String) props.get(PERSISTENCE_TYPE);

			if (PERSISTENCE_TYPE_DB.equals(type)) {
				persistenceAdapter = new DBMgmPersistenceAdapter();
			} else if (PERSISTENCE_TYPE_LOCAL.equals(type)) {
				persistenceAdapter = new LocalMgmPersistenceAdapter(props);
			}
		}

		if (persistenceAdapter == null) {
			persistenceAdapter = new LocalMgmPersistenceAdapter(props);
		}
		return persistenceAdapter;
	}

}
