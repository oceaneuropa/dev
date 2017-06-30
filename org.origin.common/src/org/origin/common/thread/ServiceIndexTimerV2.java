package org.origin.common.thread;

import java.io.IOException;

public interface ServiceIndexTimerV2<INDEX_PROVIDER, SERVICE, IDX_ITEM> {

	/**
	 * Get the service.
	 * 
	 * @return
	 */
	SERVICE getService();

	/**
	 * Get index item.
	 * 
	 * @param indexProvider
	 * @param service
	 * @return
	 * @throws IOException
	 */
	IDX_ITEM getIndex(INDEX_PROVIDER indexProvider, SERVICE service) throws IOException;

	/**
	 * Create an index item.
	 * 
	 * @param indexProvider
	 * @param service
	 * @return
	 * @throws IOException
	 */
	IDX_ITEM addIndex(INDEX_PROVIDER indexProvider, SERVICE service) throws IOException;

	/**
	 * Update index item.
	 * 
	 * @param indexProvider
	 * @param service
	 * @param indexItem
	 * @throws IOException
	 */
	void updateIndex(INDEX_PROVIDER indexProvider, SERVICE service, IDX_ITEM indexItem) throws IOException;

	/**
	 * Remove index item.
	 * 
	 * @param indexProvider
	 * @param indexItem
	 * @throws IOException
	 */
	void removeIndex(INDEX_PROVIDER indexProvider, IDX_ITEM indexItem) throws IOException;

}
