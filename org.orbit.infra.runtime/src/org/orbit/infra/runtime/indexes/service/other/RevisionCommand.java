package org.orbit.infra.runtime.indexes.service.other;

import java.util.Date;
import java.util.Map;

import org.orbit.infra.model.indexes.IndexItem;
import org.orbit.infra.model.indexes.IndexServiceException;
import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.runtime.Status;

public class RevisionCommand extends AbstractCommand {

	// runtime command names
	public static String CMD_CREATE_INDEX_ITEM = "create_index_item";
	public static String CMD_DELETE_INDEX_ITEM = "delete_index_item";
	public static String CMD_UPDATE_INDEX_ITEM = "update_index_item";

	protected IndexServiceUpdatable indexServiceUpdatable;
	protected Integer revisionId;
	protected String command;
	protected Map<String, Object> arguments;
	protected String undoCommand;
	protected Map<String, Object> undoArguments;

	/**
	 * 
	 * @param indexServiceUpdatable
	 * @param revisionId
	 * @param command
	 * @param arguments
	 * @param undoCommand
	 * @param undoArguments
	 */
	public RevisionCommand(IndexServiceUpdatable indexServiceUpdatable, Integer revisionId, String command, Map<String, Object> arguments, String undoCommand, Map<String, Object> undoArguments) {
		this.indexServiceUpdatable = indexServiceUpdatable;
		this.revisionId = revisionId;
		this.command = command;
		this.arguments = arguments;
		this.undoCommand = undoCommand;
		this.undoArguments = undoArguments;
	}

	public Integer getRevisionId() {
		return this.revisionId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		if (RevisionCommand.CMD_CREATE_INDEX_ITEM.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) this.arguments.get("indexItemId");
			String indexProviderId = (String) this.arguments.get("indexProviderId");
			String type = (String) this.arguments.get("type");
			String name = (String) this.arguments.get("name");
			Map<String, Object> properties = (Map<String, Object>) this.arguments.get("properties");
			Date createTime = (Date) this.arguments.get("createTime");
			Date lastUpdateTime = (Date) this.arguments.get("lastUpdateTime");

			// String propertiesString = (String) arguments.get("properties");
			// Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, type, name, properties, createTime, lastUpdateTime);
				this.indexServiceUpdatable.addCachedIndexItem(indexItem);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (RevisionCommand.CMD_DELETE_INDEX_ITEM.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) this.arguments.get("indexItemId");

			try {
				this.indexServiceUpdatable.removeCachedIndexItem(indexItemId);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (RevisionCommand.CMD_UPDATE_INDEX_ITEM.equalsIgnoreCase(this.command)) {
			Integer indexItemId = (Integer) this.arguments.get("indexItemId");
			Map<String, Object> properties = (Map<String, Object>) this.arguments.get("properties");
			Date lastUpdateTime = (Date) this.arguments.get("lastUpdateTime");

			// String propertiesString = (String) this.arguments.get("properties");
			// Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				this.indexServiceUpdatable.updateCachedIndexItemProperties(indexItemId, properties, lastUpdateTime);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else {
			System.out.println("SyncIndexItemCommand.execute() Unsupported command: " + this.command);
		}

		return new CommandResult(Status.OK_STATUS);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommandResult undo(CommandContext context) throws CommandException {
		if (RevisionCommand.CMD_CREATE_INDEX_ITEM.equalsIgnoreCase(this.undoCommand)) {
			Integer indexItemId = (Integer) this.undoArguments.get("indexItemId");
			String indexProviderId = (String) this.undoArguments.get("indexProviderId");
			String type = (String) this.undoArguments.get("type");
			String name = (String) this.undoArguments.get("name");
			Map<String, Object> properties = (Map<String, Object>) this.undoArguments.get("properties");
			Date createTime = (Date) this.undoArguments.get("createTime");
			Date lastUpdateTime = (Date) this.undoArguments.get("lastUpdateTime");

			// String propertiesString = (String) undoArguments.get("properties");
			// Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			IndexItem indexItem = new IndexItem(indexItemId, indexProviderId, type, name, properties, createTime, lastUpdateTime);
			try {
				this.indexServiceUpdatable.addCachedIndexItem(indexItem);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (RevisionCommand.CMD_DELETE_INDEX_ITEM.equalsIgnoreCase(this.undoCommand)) {
			Integer indexItemId = (Integer) this.undoArguments.get("indexItemId");

			try {
				this.indexServiceUpdatable.removeCachedIndexItem(indexItemId);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else if (RevisionCommand.CMD_UPDATE_INDEX_ITEM.equalsIgnoreCase(this.undoCommand)) {
			Integer indexItemId = (Integer) this.undoArguments.get("indexItemId");
			Map<String, Object> properties = (Map<String, Object>) this.arguments.get("properties");
			Date lastUpdateTime = (Date) this.undoArguments.get("lastUpdateTime");

			// String propertiesString = (String) this.undoArguments.get("properties");
			// Map<String, Object> properties = JSONUtil.toProperties(propertiesString, true);

			try {
				this.indexServiceUpdatable.updateCachedIndexItemProperties(indexItemId, properties, lastUpdateTime);
			} catch (IndexServiceException e) {
				e.printStackTrace();
				throw new CommandException(e);
			}

		} else {
			System.out.println("RevisionCommand.undo() Unsupported command: " + this.command);
		}

		return new CommandResult(Status.OK_STATUS);
	}

}
