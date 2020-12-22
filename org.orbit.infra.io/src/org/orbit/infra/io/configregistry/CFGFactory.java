package org.orbit.infra.io.configregistry;

import org.orbit.infra.io.configregistry.impl.CFGFactoryImpl;

public abstract class CFGFactory {

	protected static CFGFactory INSTANCE = init();

	protected static CFGFactory init() {
		CFGFactory factory = new CFGFactoryImpl();
		return factory;
	}

	public synchronized static void setInstance(CFGFactory factory) {
		INSTANCE = factory;
	}

	public synchronized static CFGFactory getInstance() {
		if (INSTANCE == null) {
			INSTANCE = init();
		}
		return INSTANCE;
	}

	public abstract CFG createCFG(String accessToken);

}
