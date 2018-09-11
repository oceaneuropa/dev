package org.orbit.infra.api.indexes;

public interface ServiceIndexTimerFactory<SERVICE> {

	public static final String EXTENSION_TYPE_ID = "infra.extension.IndexProvider";

	ServiceIndexTimer<SERVICE> create(IndexServiceClient indexProvider, SERVICE service);

}
