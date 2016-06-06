package org.origin.mgm.service.command;

import java.util.Date;
import java.util.Map;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.json.JSONUtil;
import org.origin.mgm.exception.IndexServiceException;
import org.origin.mgm.model.runtime.IndexItem;
import org.origin.mgm.service.IndexServiceUpdatable;

public class RevisionCommand extends AbstractCommand {

	public final static String CREATE_INDEX_ITEM_COMMAND = "create_index_item";
	public final static String DELETE_INDEX_ITEM_COMMAND = "delete_index_item";
	public final static String UPDATE_INDEX_ITEM_COMMAND = "update_index_item";

	protected IndexServiceUpdatable indexServiceUpdatable;
	protected Integer revisionId;
	protected String command;
	protected String argumentsString;
	protected String undoCommand;
	protected String undoArgumentsString;

	/**
	 * 
	 * @param indexServiceUpdatable
	 * @param revisionId
	 * @param command
	 * @param argumentsString
	 * @param undoCommand
	 * @param undoArgumentsString
	 */
	public RevisionCommand(IndexServiceUpdatable indexServiceUpdatable, Integer revisionId, String command, String argumentsString, String undoCommand, String undoArgumentsString) {
		this.indexServiceUpdatable = indexServiceUpdatable;
		this.revisionId = revisionId;
		this.command = command;
		this.argumentsString = argumentsString;
		this.undoCommand = undoCommand;
		this.undoArgumentsString = undoArgumentsString;
	}

	public Integer getRevisionId() {
		return this.revisionId;
	}

	@Override
	public void execute(CommandContext context) throws CommandException {
		Map<String, Object> arguments = JSONUtil.toProperties(argumentsString, true);

		if (CREATE_INDEX_ITEM_COMMAND.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) arguments.get("indexItemId");
			String indexProviderId = (String) arguments.get("indexProviderId");
			String namespace = (String) arguments.get("namespace");
			String name = (String) arguments.get("name");
			String propertiesString = (String) arguments.get("properties");
			Date createTime = (Date) arguments.get("createTime");
			Date lastUpdateTime = (Date) arguments.get("lastUpdateTime");

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, namespace, name, properties, createTime, lastUpdateTime);
				this.indexServiceUpdatable.addCachedIndexItem(indexItem);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (DELETE_INDEX_ITEM_COMMAND.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) arguments.get("indexItemId");

			try {
				this.indexServiceUpdatable.removeCachedIndexItem(indexItemId);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (UPDATE_INDEX_ITEM_COMMAND.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) arguments.get("indexItemId");
			String propertiesString = (String) arguments.get("properties");
			Date lastUpdateTime = (Date) arguments.get("lastUpdateTime");

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				this.indexServiceUpdatable.udpateCachedIndexItem(indexItemId, properties, lastUpdateTime);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else {
			System.out.println("SyncIndexItemCommand.execute() Unsupported command: " + this.command);
		}
	}

	@Override
	public void undo(CommandContext context) throws CommandException {
		Map<String, Object> undoArguments = JSONUtil.toProperties(undoArgumentsString, true);

		if (CREATE_INDEX_ITEM_COMMAND.equalsIgnoreCase(this.undoCommand)) {
			Integer indexItemId = (Integer) undoArguments.get("indexItemId");
			String indexProviderId = (String) undoArguments.get("indexProviderId");
			String namespace = (String) undoArguments.get("namespace");
			String name = (String) undoArguments.get("name");
			String propertiesString = (String) undoArguments.get("properties");
			Date createTime = (Date) undoArguments.get("createTime");
			Date lastUpdateTime = (Date) undoArguments.get("lastUpdateTime");

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, namespace, name, properties, createTime, lastUpdateTime);
			try {
				this.indexServiceUpdatable.addCachedIndexItem(indexItem);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (UPDATE_INDEX_ITEM_COMMAND.equalsIgnoreCase(this.undoCommand)) {
			Integer indexItemId = (Integer) undoArguments.get("indexItemId");
			String propertiesString = (String) undoArguments.get("properties");
			Date lastUpdateTime = (Date) undoArguments.get("lastUpdateTime");

			Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				this.indexServiceUpdatable.udpateCachedIndexItem(indexItemId, properties, lastUpdateTime);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else {
			System.out.println("SyncIndexItemCommand.undo() Unsupported command: " + this.command);
		}
	}

}
