package other.orbit.infra.api.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexProviderClient;

public interface IndexProviderConnectorV1 {

	IndexProviderClient getService(Map<Object, Object> properties);

}
