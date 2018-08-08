package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.SensorEntity;

public class SensorPersistence extends Persistencer<SensorEntity, String> {

	public SensorPersistence() {
		this.entityClass = SensorEntity.class;
	}

}
