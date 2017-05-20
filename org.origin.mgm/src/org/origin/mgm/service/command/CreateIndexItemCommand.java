package org.origin.mgm.service.command;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.origin.mgm.persistence.IndexItemRequestTableHandler.STATUS_PENDING;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.origin.common.command.AbstractCommand;
import org.origin.common.command.CommandContext;
import org.origin.common.command.CommandException;
import org.origin.common.command.impl.CommandResult;
import org.origin.common.jdbc.DatabaseUtil;
import org.origin.common.json.JSONUtil;
import org.origin.common.runtime.Status;
import org.origin.common.util.ExceptionUtil;
import org.origin.mgm.model.vo.IndexItemVO;
import org.origin.mgm.model.vo.IndexItemRequestVO;
import org.origin.mgm.persistence.IndexItemDataTableHandler;
import org.origin.mgm.persistence.IndexItemRequestTableHandler;
import org.origin.mgm.persistence.IndexItemRevisionTableHandler;

/**
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
			String indexProviderId = (String) arguments.get("indexProviderId");
			String type = (String) arguments.get("type");
			String name = (String) arguments.get("name");
			ExceptionUtil.checkNotNullAndNotEmpty(indexProviderId, IllegalArgumentException.class, null, null);
			ExceptionUtil.checkNotNullAndNotEmpty(type, IllegalArgumentException.class, null, null);
			ExceptionUtil.checkNotNullAndNotEmpty(name, IllegalArgumentException.class, null, null);

			command = new CreateIndexItemCommand(indexProviderId, type, name);

		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return command;
	}

	protected String indexProviderId;
	protected String type;
	protected String name;
	protected Map<String, Object> properties;

	protected IndexItemRequestTableHandler requestTableHandler = IndexItemRequestTableHandler.INSTANCE;
	protected IndexItemDataTableHandler dataTableHandler = IndexItemDataTableHandler.INSTANCE;
	protected IndexItemRevisionTableHandler revisionTableHandler = IndexItemRevisionTableHandler.INSTANCE;

	protected IndexItemRequestVO newRequestVO;
	protected IndexItemVO newIndexItemVO;

	protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	/**
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 */
	public CreateIndexItemCommand(String indexProviderId, String type, String name) {
		this(indexProviderId, type, name, null);
	}

	/**
	 * 
	 * @param indexProviderId
	 * @param type
	 * @param name
	 * @param properties
	 */
	public CreateIndexItemCommand(String indexProviderId, String type, String name, Map<String, Object> properties) {
		this.indexProviderId = indexProviderId;
		this.type = type;
		this.name = name;
		this.properties = properties;
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
	}

	protected void reset() {
		this.newRequestVO = null;
		this.newIndexItemVO = null;
	}

	@Override
	public CommandResult execute(CommandContext context) throws CommandException {
		final ScheduledFuture<?>[] requestUpdaterHandle = new ScheduledFuture<?>[1];

		final Connection conn = context.getAdapter(Connection.class);
		ExceptionUtil.checkNotNull(conn, null, null);
		try {
			// -------------------------------------------------------------------------------------------------------
			// Step0. Prepare parameters
			// -------------------------------------------------------------------------------------------------------
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("indexProviderId", this.indexProviderId);
			arguments.put("type", this.type);
			arguments.put("name", this.name);
			String argumentsString = JSONUtil.toJsonString(arguments);

			this.properties.putAll(arguments);
			String propertiesString = JSONUtil.toJsonString(properties);

			// -------------------------------------------------------------------------------------------------------
			// Step1. Create a new request
			// -------------------------------------------------------------------------------------------------------
			boolean requestTableHandler_insert_has_exception = false;
			try {
				this.newRequestVO = requestTableHandler.insert(conn, indexProviderId, COMMAND_NAME, argumentsString, STATUS_PENDING, new Date(), null);
			} catch (SQLException e) {
				e.printStackTrace();
				requestTableHandler_insert_has_exception = true;
			}
			if (requestTableHandler_insert_has_exception || newRequestVO == null) {
				// Runtime exception
				throw new CommandException("New index item request cannot be created.");
			}

			final Integer requestId = newRequestVO.getRequestId();
			System.out.println("New request is created.");
			System.out.println(newRequestVO);

			// -------------------------------------------------------------------------------------------------------
			// Step2. start updating the request's lastUpdateTime every second.
			// -------------------------------------------------------------------------------------------------------
			Runnable requestUpdater = new Runnable() {
				@Override
				public void run() {
					System.out.println("Update request (requestId=" + requestId + ") lastUpdateTime.");
					try {
						requestTableHandler.updateLastUpdateTime(conn, requestId, new Date());
					} catch (SQLException e) {
						e.printStackTrace();
						// Runtime exception
					}
				}
			};
			// update request's lastUpdateTime every second. initial delay 1 second
			requestUpdaterHandle[0] = scheduler.scheduleAtFixedRate(requestUpdater, 1, 1, SECONDS);

			// -------------------------------------------------------------------------------------------------------
			// Step3. Check if there are previous created pending requests that were still active within 10 seconds.
			// Wait if such requests still exist.
			// -------------------------------------------------------------------------------------------------------
			long totalWaitingTime = 0;
			while (true) {
				// check whether there are predecessor requests that are still pending and active (lastUpdateTime within 10 seconds)
				boolean hasActivePendingPredecessorRequests = false;
				try {
					List<IndexItemRequestVO> activePendingRequests = requestTableHandler.getActivePendingRequests(conn, requestId, 10);
					if (!activePendingRequests.isEmpty()) {
						hasActivePendingPredecessorRequests = true;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					try {
						cancelRequestUpdaterAndMarkRequestAsCancelled(conn, requestId, requestUpdaterHandle[0]);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					// Runtime exception
					throw new CommandException("Pending index item requests cannot be retrieved.");
				}
				if (!hasActivePendingPredecessorRequests) {
					// Stops waiting if there is active no predecessor pending requests.
					// This command is ready to execute at this time.
					break;
				}

				// Wait for half second and then check again.
				try {
					Thread.sleep(500);
					totalWaitingTime += 500;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Stops waiting if total waiting time exceed 10 seconds. 10 seconds should be long enough for any single command to complete.
				if (totalWaitingTime > 10 * 1000) {
					System.out.println("Total waiting time exceeds 10 seconds.");
					break;
				}
			} // while

			// -------------------------------------------------------------------------------------------------------
			// Step 4. Create index item data and get the new indexItemId.
			// -------------------------------------------------------------------------------------------------------
			boolean dataTableHandler_insert_has_exception = false;
			try {
				this.newIndexItemVO = dataTableHandler.insert(conn, argumentsString, argumentsString, argumentsString, propertiesString, new Date(), null);
			} catch (SQLException e) {
				e.printStackTrace();
				dataTableHandler_insert_has_exception = true;
			}
			if (dataTableHandler_insert_has_exception || this.newIndexItemVO == null) {
				try {
					cancelRequestUpdaterAndMarkRequestAsCancelled(conn, requestId, requestUpdaterHandle[0]);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				// This is considered as Runtime exception
				throw new CommandException("New index item cannot be created.");
			}
			System.out.println("New index item is created.");
			System.out.println(newIndexItemVO);
			Integer indexItemId = newIndexItemVO.getIndexItemId();

			// -------------------------------------------------------------------------------------------------------
			// Step 5. Update revision table
			// -------------------------------------------------------------------------------------------------------

			// -------------------------------------------------------------------------------------------------------
			// Step 6. Create undo Command and return the undo Command.
			// -------------------------------------------------------------------------------------------------------

			// -------------------------------------------------------------------------------------------------------
			// Step 7. Stops the requestUpdater. Update the request as completed or cancelled (with lastUpdateTime).
			// -------------------------------------------------------------------------------------------------------
			if (requestUpdaterHandle[0] != null) {
				requestUpdaterHandle[0].cancel(true);
			}
			try {
				requestTableHandler.updateStatusAsCompleted(conn, requestId, new Date());
			} catch (SQLException e) {
				e.printStackTrace();
				// This is considered as Runtime exception
				throw new CommandException("New request cannot be set as completed.");
			}

		} finally {
			DatabaseUtil.closeQuietly(conn, true);
		}

		return new CommandResult(this, Status.OK_STATUS);
	}

	/**
	 * 
	 * @param conn
	 * @param requestId
	 * @param requestUpdaterHandle
	 * @throws SQLException
	 */
	protected void cancelRequestUpdaterAndMarkRequestAsCancelled(Connection conn, Integer requestId, ScheduledFuture<?> requestUpdaterHandle) throws SQLException {
		// cancel the updater
		if (requestUpdaterHandle != null) {
			requestUpdaterHandle.cancel(true);
		}
		// mark the request status as cancelled
		requestTableHandler.updateStatusAsCancelled(conn, requestId, new Date());
	}

	@Override
	public CommandResult undo(CommandContext context) throws CommandException {
		return new CommandResult(this, Status.OK_STATUS);
	}

}
