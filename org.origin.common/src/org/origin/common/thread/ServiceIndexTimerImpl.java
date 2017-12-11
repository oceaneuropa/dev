package org.origin.common.thread;

import java.io.IOException;

import org.origin.common.util.Timer;

public abstract class ServiceIndexTimerImpl<INDEX_PROVIDER, SERVICE, IDX_ITEM> extends ThreadPoolTimer implements ServiceIndexTimer<INDEX_PROVIDER, SERVICE, IDX_ITEM> {

	/* update index items every 15 seconds */
	public static long INDEXING_INTERVAL = 15 * Timer.SECOND;

	protected INDEX_PROVIDER indexProvider;
	protected IDX_ITEM indexItem;
	protected boolean removeIndexWhenDisposed = true;

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimerImpl(String name, INDEX_PROVIDER indexProvider) {
		super(name);
		this.indexProvider = indexProvider;

		setInterval(INDEXING_INTERVAL);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				performIndexing();
			}
		};
		setRunnable(runnable);
	}

	public INDEX_PROVIDER getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(INDEX_PROVIDER indexProvider) {
		this.indexProvider = indexProvider;
	}

	public boolean isRemoveIndexWhenDisposed() {
		return this.removeIndexWhenDisposed;
	}

	public void setRemoveIndexWhenDisposed(boolean removeIndexWhenDisposed) {
		this.removeIndexWhenDisposed = removeIndexWhenDisposed;
	}

	protected synchronized void performIndexing() {
		String name = getName();

		if (this.indexProvider == null) {
			System.err.println(name + " indexProvider is null.");
			return;
		}

		SERVICE service = getService();
		if (service == null) {
			System.err.println(name + " service is null.");
			return;
		}

		try {
			if (this.indexItem == null) {
				this.indexItem = getIndex(this.indexProvider, service);
			}
			if (this.indexItem == null) {
				this.indexItem = addIndex(this.indexProvider, service);
			} else {
				updateIndex(this.indexProvider, service, this.indexItem);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		super.dispose();

		if (this.removeIndexWhenDisposed) {
			if (this.indexProvider != null && this.indexItem != null) {
				try {
					removeIndex(this.indexProvider, this.indexItem);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
