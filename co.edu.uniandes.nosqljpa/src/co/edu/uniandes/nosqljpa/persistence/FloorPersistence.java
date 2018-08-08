package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.FloorEntity;

public class FloorPersistence extends Persistencer<FloorEntity, String> {

	public FloorPersistence() {
		this.entityClass = FloorEntity.class;
	}

}
