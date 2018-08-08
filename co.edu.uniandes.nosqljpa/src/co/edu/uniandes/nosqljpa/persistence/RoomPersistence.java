package co.edu.uniandes.nosqljpa.persistence;

import co.edu.uniandes.nosqljpa.model.entity.RoomEntity;

public class RoomPersistence extends Persistencer<RoomEntity, String> {

	public RoomPersistence() {
		this.entityClass = RoomEntity.class;
	}

}
