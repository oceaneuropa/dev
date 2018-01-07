package org.orbit.infra.api.indexes.other;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexService;

public interface IndexServiceConnectorV1 {

	IndexService getService(Map<Object, Object> properties);

}
