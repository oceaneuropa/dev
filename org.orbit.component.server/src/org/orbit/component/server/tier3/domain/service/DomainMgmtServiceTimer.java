package org.orbit.component.server.tier3.domain.service;

import java.io.IOException;

import org.origin.common.thread.ServiceIndexTimer;
import org.origin.common.thread.ServiceIndexTimerImpl;
import org.origin.mgm.client.api.IndexProvider;

public class DomainMgmtServiceTimer extends ServiceIndexTimerImpl<IndexProvider, DomainMgmtService> implements ServiceIndexTimer<IndexProvider, DomainMgmtService> {

	public DomainMgmtServiceTimer(IndexProvider indexProvider) {
		super("Domain Management Service Index Timer", indexProvider);
	}

	@Override
	public DomainMgmtService getService() {
		return null;
	}

	@Override
	public void updateIndex(IndexProvider indexProvider, DomainMgmtService service) throws IOException {

	}

}
