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
	public static MgmPersistenceAdapter createInstance(Map<Object, Object> props) {
		MgmPersistenceAdapter persistenceAdapter = null;
		if (props != null && props.get(PERSISTENCE_TYPE) instanceof String) {
			String type = (String) props.get(PERSISTENCE_TYPE);

			if (PERSISTENCE_TYPE_DB.equals(type)) {
				persistenceAdapter = new DBPersistenceAdapter();
			} else if (PERSISTENCE_TYPE_LOCAL.equals(type)) {
				persistenceAdapter = new LocalPersistenceAdapter(props);
			}
		}

		if (persistenceAdapter == null) {
			persistenceAdapter = new LocalPersistenceAdapter(props);
		}
		return persistenceAdapter;
	}

}
