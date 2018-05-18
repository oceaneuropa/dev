package other.orbit.infra.runtime.indexes.service;

import java.sql.Connection;

import javax.xml.namespace.QName;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.runtime.Status;
import org.origin.common.util.ExceptionUtil;

public class DeleteIndexItemCommand extends AbstractCommand {

	public final static String COMMAND_NAME = "delete_index_item";

	protected String indexProviderId;
	protected QName qName;

	/**
	 * 
	 * @param indexProviderId
	 * @param qName
	 */
	public DeleteIndexItemCommand() {
		// this.indexProviderId = indexProviderId;
		// this.qName = qName;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		Connection conn = context.getAdapter(Connection.class);
		ExceptionUtil.checkNotNull(conn, null, null);
		try {

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}
		return new CommandResult(Status.OK_STATUS);
	}

	@Override
	public CommandResult undo(CommandContext context) throws CommandException {
		return new CommandResult(Status.OK_STATUS);
	}

}
