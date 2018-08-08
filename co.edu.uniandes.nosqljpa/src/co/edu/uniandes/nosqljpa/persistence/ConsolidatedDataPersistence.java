package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.ConsolidatedDataEntity;

public class ConsolidatedDataPersistence extends Persistencer<ConsolidatedDataEntity, String> {

	public ConsolidatedDataPersistence() {
		this.entityClass = ConsolidatedDataEntity.class;
	}

}
