package org.origin.mgm.service.command;

import java.sql.Connection;
import java.util.Map;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.util.ExceptionUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class CreateIndexItemCommand extends AbstractCommand {

	public final static String COMMAND_NAME = "create_index_item";

	/**
	 * Parse arguments into a CreateIndexItemCommand object.
	 * 
	 * @param arguments
	 * @return
	 */
	public static CreateIndexItemCommand parseCommand(Map<String, Object> arguments) {
		CreateIndexItemCommand command = null;
		try {
			String indexProviderId = (String) arguments.get("indexproviderid");
			String namespace = (String) arguments.get("namespace");
			String name = (String) arguments.get("name");
			ExceptionUtil.checkNotNullAndNotEmpty(indexProviderId, IllegalArgumentException.class, null, null);
			ExceptionUtil.checkNotNullAndNotEmpty(namespace, IllegalArgumentException.class, null, null);
			ExceptionUtil.checkNotNullAndNotEmpty(name, IllegalArgumentException.class, null, null);

			command = new CreateIndexItemCommand(indexProviderId, namespace, name);

		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return command;
	}

	protected String indexProviderId;
	protected String namespace;
	protected String name;

	/**
	 * 
	 * @param indexProviderId
	 * @param namespace
	 * @param name
	 */
	public CreateIndexItemCommand(String indexProviderId, String namespace, String name) {
		this.indexProviderId = indexProviderId;
		this.namespace = namespace;
		this.name = name;
	}

	@Override
	public AbstractCommand execute(CommandContext context) throws CommandException {
		Connection conn = context.getAdapter(Connection.class);
		ExceptionUtil.checkNotNull(conn, null, null);
		try {

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return null;
	}

}
