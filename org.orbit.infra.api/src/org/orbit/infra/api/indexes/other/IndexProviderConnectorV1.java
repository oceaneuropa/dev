package org.orbit.infra.api.indexes.other;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProvider;

public interface IndexProviderConnectorV1 {

	IndexProvider getService(Map<Object, Object> properties);

}
