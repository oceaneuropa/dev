package org.orbit.infra.api.indexes;

public interface ServiceIndexTimerFactory<SERVICE> {

	ServiceIndexTimer<SERVICE> create(IndexProvider indexProvider, SERVICE service);

}

// public static final String TYPE_ID = "infra.extension.IndexProvider";
// public static final String PROP_INDEX_PROVIDER_ID = "indexProviderId";
