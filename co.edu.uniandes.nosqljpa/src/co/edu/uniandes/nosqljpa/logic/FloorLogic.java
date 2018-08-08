package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.FloorConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.IFloorLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.FloorDTO;
import co.edu.uniandes.nosqljpa.persistence.FloorPersistence;

public class FloorLogic implements IFloorLogic {

	private final FloorPersistence persistence;

	public FloorLogic() {
		this.persistence = new FloorPersistence();
	}

	@Override
	public FloorDTO add(FloorDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		FloorDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public FloorDTO update(FloorDTO dto) {
		FloorDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public FloorDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public FloorDTO findCode(String code) {
		return CONVERTER.entityToDto(persistence.findCode(code));
	}

	@Override
	public List<FloorDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}
}