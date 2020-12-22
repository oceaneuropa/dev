package org.orbit.infra.io.configregistry.impl;

import org.orbit.infra.io.configregistry.CFG;
import org.orbit.infra.io.configregistry.CFGFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class CFGFactoryImpl extends CFGFactory {

	@Override
	public CFG createCFG(String accessToken) {
		return new CFGImpl(accessToken);
	}

}
