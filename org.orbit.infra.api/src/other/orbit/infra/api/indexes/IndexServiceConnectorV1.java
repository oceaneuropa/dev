package other.orbit.infra.api.indexes;

import java.util.Map;

import org.orbit.infra.api.indexes.IndexService;

public interface IndexServiceConnectorV1 {

	IndexService getService(Map<Object, Object> properties);

}
