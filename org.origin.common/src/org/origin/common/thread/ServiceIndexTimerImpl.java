package org.origin.common.thread;

import java.io.IOException;

public abstract class ServiceIndexTimerImpl<INDEX_PROVIDER, SERVICE> extends ThreadPoolTimer implements ServiceIndexTimer<INDEX_PROVIDER, SERVICE> {

	protected INDEX_PROVIDER indexProvider;

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimerImpl(String name, INDEX_PROVIDER indexProvider) {
		super(name);
		if (indexProvider == null) {
			throw new IllegalArgumentException("indexProvider is null.");
		}
		this.indexProvider = indexProvider;

		setInterval(30 * 1000); // 30 seconds

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
				String name = ServiceIndexTimerImpl.this.getName();
				INDEX_PROVIDER indexProvider = ServiceIndexTimerImpl.this.indexProvider;
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
		return indexProvider;
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

}
