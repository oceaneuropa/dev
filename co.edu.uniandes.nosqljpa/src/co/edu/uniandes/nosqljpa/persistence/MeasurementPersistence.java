package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.MeasurementEntity;

public class MeasurementPersistence extends Persistencer<MeasurementEntity, String> {

	public MeasurementPersistence() {
		this.entityClass = MeasurementEntity.class;
	}

}
