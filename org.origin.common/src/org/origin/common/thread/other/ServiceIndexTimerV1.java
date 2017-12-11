package org.origin.common.thread.other;

import java.io.IOException;

public interface ServiceIndexTimerV1<INDEX_PROVIDER, SERVICE> {

	/**
	 * Get the service.
	 * 
	 * @return
	 */
	SERVICE getService();

	/**
	 * Create or update the index item for the service.
	 * 
	 * @param indexProvider
	 * @param service
	 * @throws IOException
	 */
	void updateIndex(INDEX_PROVIDER indexProvider, SERVICE service) throws IOException;

}
