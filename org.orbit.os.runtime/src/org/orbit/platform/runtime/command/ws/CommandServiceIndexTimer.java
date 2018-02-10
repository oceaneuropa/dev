package org.orbit.platform.runtime.command.ws;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.orbit.infra.api.indexes.IndexItem;
import org.orbit.infra.api.indexes.IndexProvider;
import org.orbit.platform.runtime.OSConstants;
import org.orbit.platform.runtime.command.service.CommandService;
import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;

public class CommandServiceIndexTimer extends ServiceIndexTimerImpl<IndexProvider, CommandService, IndexItem> implements ServiceIndexTimer<IndexProvider, CommandService, IndexItem> {

	protected CommandService service;

	/**
	 * 
	 * @param indexProvider
	 * @param service
	 */
	public CommandServiceIndexTimer(IndexProvider indexProvider, CommandService service) {
		super("Index Timer [" + service.getName() + "]", indexProvider);
		setDebug(false);
		this.service = service;
	}

	@Override
	public CommandService getService() {
		return this.service;
	}

	@Override
	public IndexItem getIndex(IndexProvider indexProvider, CommandService service) throws IOException {
		String name = service.getName();

		return indexProvider.getIndexItem(OSConstants.COMMAND_SERVICE_INDEXER_ID, OSConstants.COMMAND_SERVICE_TYPE, name);
	}

	@Override
	public IndexItem addIndex(IndexProvider indexProvider, CommandService service) throws IOException {
		String name = service.getName();
		String contextRoot = service.getContextRoot();

		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OSConstants.COMMAND_SERVICE_NAME, name);
		props.put(OSConstants.COMMAND_SERVICE_CONTEXT_ROOT, contextRoot);
		props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		return indexProvider.addIndexItem(OSConstants.COMMAND_SERVICE_INDEXER_ID, OSConstants.COMMAND_SERVICE_TYPE, name, props);
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, CommandService service, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();
		Map<String, Object> props = new Hashtable<String, Object>();
		props.put(OSConstants.LAST_HEARTBEAT_TIME, new Date().getTime());

		indexProvider.setProperties(OSConstants.COMMAND_SERVICE_INDEXER_ID, indexItemId, props);
	}

	@Override
	public void removeIndex(IndexProvider indexProvider, IndexItem indexItem) throws IOException {
		Integer indexItemId = indexItem.getIndexItemId();

		indexProvider.removeIndexItem(OSConstants.COMMAND_SERVICE_INDEXER_ID, indexItemId);
	}

}
