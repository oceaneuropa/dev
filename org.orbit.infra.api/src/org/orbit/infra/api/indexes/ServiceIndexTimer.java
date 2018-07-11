package org.orbit.infra.api.indexes;

import java.io.IOException;

import org.origin.common.thread.IndexTimerImpl;

public abstract class ServiceIndexTimer<SERVICE> extends IndexTimerImpl<IndexProvider, SERVICE, IndexItem> {

	/**
	 * 
	 * @param name
	 * @param indexProvider
	 */
	public ServiceIndexTimer(String name, IndexProvider indexProvider) {
		super(name, indexProvider);
	}

	@Override
	protected synchronized void performIndexing() {
		if (this.indexProvider != null) {
			try {
				String time1 = String.valueOf(System.currentTimeMillis());
				String time2 = this.indexProvider.echo(time1);
				if (time1.equals(time2)) {
					super.performIndexing();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
