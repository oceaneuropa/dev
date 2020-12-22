package org.orbit.infra.io.configregistry;

import org.orbit.infra.io.CFG;
import org.orbit.infra.io.configregistry.impl.CFGFactoryImpl;

public abstract class CFGFactory {

	public static CFGFactory INSTANCE = init();

	public static CFGFactory init() {
		CFGFactory factory = new CFGFactoryImpl();
		return factory;
	}

	public synchronized static void setFactory(CFGFactory factory) {
		INSTANCE = factory;
	}

	public synchronized static CFGFactory getFactory() {
		if (INSTANCE == null) {
			INSTANCE = init();
		}
		return INSTANCE;
	}

	public abstract CFG createCFG(String accessToken);

}
