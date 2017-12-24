package org.orbit.component.runtime.tier1.auth.lbr;

import org.orbit.component.runtime.common.ws.OrbitWSApplication;

/**
 * 
 * @see https://www.programcreek.com/java-api-examples/index.php?source_dir=para-master/para-server/src/main/java/com/erudika/para/rest/Api1.java
 *
 */
public class AuthWSApplicationLBR extends OrbitWSApplication {

	protected AuthServiceLBR lbr;

	/**
	 * 
	 * @param contextRoot
	 * @param feature
	 */
	public AuthWSApplicationLBR(String contextRoot, AuthServiceLBR lbr, int feature) {
		super(contextRoot, feature);
	}

}
