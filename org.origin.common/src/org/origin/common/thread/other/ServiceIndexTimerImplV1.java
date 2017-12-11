package org.origin.common.thread.other;

import java.io.IOException;

import org.origin.common.thread.ThreadPoolTimer;
import org.origin.common.util.Timer;

public abstract class ServiceIndexTimerImplV1<INDEX_PROVIDER, SERVICE> extends ThreadPoolTimer implements ServiceIndexTimerV1<INDEX_PROVIDER, SERVICE> {

	/* update index items every 15 seconds */
	public static long INDEXING_INTERVAL = 15 * Timer.SECOND;

	protected INDEX_PROVIDER indexProvider;

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimerImplV1(String name, INDEX_PROVIDER indexProvider) {
		super(name);
		if (indexProvider == null) {
			throw new IllegalArgumentException("indexProvider is null.");
		}
		this.indexProvider = indexProvider;

		setInterval(INDEXING_INTERVAL);

		Runnable runnable = createRunnable();
		setRunnable(runnable);
	}

	/**
	 * 
	 * @return
	 */
	protected Runnable createRunnable() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String name = ServiceIndexTimerImplV1.this.getName();
				INDEX_PROVIDER indexProvider = ServiceIndexTimerImplV1.this.indexProvider;
				SERVICE service = getService();

				if (indexProvider == null) {
					System.err.println(name + " indexProvider is null.");
					return;
				}

				if (service == null) {
					System.err.println(name + " service is null.");
					return;
				}

				try {
					updateIndex(indexProvider, service);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		return runnable;
	}

	public INDEX_PROVIDER getIndexProvider() {
		return this.indexProvider;
	}

	public void setIndexProvider(INDEX_PROVIDER indexProvider) {
		this.indexProvider = indexProvider;
	}

	/**
	 * Get the service.
	 * 
	 * @return
	 */
	public abstract SERVICE getService();

	/**
	 * Create or update the index item for the service.
	 * 
	 * @param indexProvider
	 * @param service
	 * @throws IOException
	 */
	public abstract void updateIndex(INDEX_PROVIDER indexProvider, SERVICE service) throws IOException;

	/**
	 * Delete the index item for the service.
	 * 
	 * @param indexProvider
	 * @throws IOException
	 */
	public void removeIndex(INDEX_PROVIDER indexProvider) throws IOException {
	}

	@Override
	public void dispose() {
		super.dispose();

		String name = getName();
		INDEX_PROVIDER indexProvider = ServiceIndexTimerImplV1.this.indexProvider;
		// SERVICE service = getService();
		if (indexProvider == null) {
			System.err.println(name + " indexProvider is null.");
			return;
		}
		try {
			removeIndex(indexProvider);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
