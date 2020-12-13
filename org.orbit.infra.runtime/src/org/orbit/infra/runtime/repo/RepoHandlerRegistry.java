package org.orbit.infra.runtime.repo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class RepoHandlerRegistry {

	protected static RepoHandlerRegistry INSTANCE = new RepoHandlerRegistry();

	public static RepoHandlerRegistry getInstsance() {
		return INSTANCE;
	}

	public static void setInstsance(RepoHandlerRegistry instance) {
		INSTANCE = instance;
	}

	protected List<RepoHandler> handlers = new ArrayList<RepoHandler>();

	public synchronized RepoHandler[] getRepoHandlers() {
		return this.handlers.toArray(new RepoHandler[this.handlers.size()]);
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public synchronized RepoHandler getRepoHandler(String type) {
		RepoHandler handler = null;
		if (type != null) {
			for (RepoHandler currHandler : this.handlers) {
				String currType = currHandler.getType();
				if (type.equals(currType)) {
					handler = currHandler;
					break;
				}
			}
		}
		return handler;
	}

	/**
	 * 
	 * @param handler
	 * @return
	 */
	public synchronized boolean addRepoHandler(RepoHandler handler) {
		boolean succeed = false;
		if (handler != null && !this.handlers.contains(handler)) {
			succeed = this.handlers.add(handler);
		}
		return succeed;
	}

	/**
	 * 
	 * @param handler
	 * @return
	 */
	public synchronized boolean removeRepoHandler(RepoHandler handler) {
		boolean succeed = false;
		if (handler != null && this.handlers.contains(handler)) {
			succeed = this.handlers.remove(handler);
		}
		return succeed;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public synchronized RepoHandler removeRepoHandler(String type) {
		RepoHandler handler = null;
		if (type != null) {
			for (RepoHandler currHandler : this.handlers) {
				String currType = currHandler.getType();
				if (type.equals(currType)) {
					handler = currHandler;
					break;
				}
			}
			if (handler != null) {
				this.handlers.remove(handler);
			}
		}
		return handler;
	}

}
