package org.orbit.platform.runtime.core.command;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.core.Response;

import org.orbit.platform.runtime.Activator;
import org.orbit.platform.runtime.core.Platform;
import org.origin.common.rest.editpolicy.AbstractWSCommand;
import org.origin.common.rest.model.Request;
import org.origin.common.rest.util.RequestUtil;
import org.osgi.framework.BundleContext;

public class ShutdownPlatformCommand extends AbstractWSCommand {

	protected Platform platform;
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	protected Timer timer;
	protected TimerTask timerTask;

	/**
	 * 
	 * @param platform
	 */
	public ShutdownPlatformCommand(Platform platform) {
		this.platform = platform;
	}

	@Override
	public Response execute(Request request) throws Exception {
		boolean force = RequestUtil.getParameter(request, "force", Boolean.class, null);
		long timeout = RequestUtil.getParameter(request, "timeout", Long.class, (long) 0);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					BundleContext bundleContext = Activator.getInstance().getBundleContext();
					if (bundleContext != null) {
						// Stop the current bundle
						Activator.getInstance().stop(bundleContext);

						// Stop the framework
						bundleContext.getBundle(0).stop();

						System.exit(1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.executorService.execute(runnable);

		if (timeout > 0) {
			this.timerTask = new TimerTask() {
				@Override
				public void run() {
					// Exit jvm
					System.exit(1);
				}
			};
			this.timer = new Timer(true);
			this.timer.schedule(this.timerTask, timeout);
		}

		if (force) {
			System.exit(1);
		}
		return null;
	}

}

// Map<String, Boolean> result = new HashMap<String, Boolean>();
// result.put("succeed", succeed);
// return Response.status(Status.OK).entity(result).build();
