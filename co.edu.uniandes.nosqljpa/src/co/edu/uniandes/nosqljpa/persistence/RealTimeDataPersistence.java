package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.RealTimeDataEntity;

public class RealTimeDataPersistence extends Persistencer<RealTimeDataEntity, String> {

	public RealTimeDataPersistence() {
		this.entityClass = RealTimeDataEntity.class;
	}

}
