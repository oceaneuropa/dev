package other.orbit.infra.api.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexServiceClient;

public interface IndexServiceConnectorV1 {

	IndexServiceClient getService(Map<Object, Object> properties);

}
