package org.origin.common.rest.client;

import java.util.HashMap;
import java.util.Map;

class Realms {

	private static Map<String, Realm> realms = new HashMap<String, Realm>();

	synchronized static Realm getRealm(String name) {
		Realm realm = realms.get(name);
		if (realm == null) {
			realm = new RealmImpl(name);
			realms.put(name, realm);
		}
		return realm;
	}

}
