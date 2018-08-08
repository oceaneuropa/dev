package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.RoomConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.IRoomLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;
import co.edu.uniandes.nosqljpa.persistence.RoomPersistence;

public class RoomLogic implements IRoomLogic {

	private final RoomPersistence persistence;

	public RoomLogic() {
		this.persistence = new RoomPersistence();
	}

	@Override
	public RoomDTO add(RoomDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		RoomDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public RoomDTO update(RoomDTO dto) {
		RoomDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public RoomDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public List<RoomDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}

	@Override
	public RoomDTO findCode(String code) {
		return CONVERTER.entityToDto(persistence.findCode(code));
	}

}
