package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.SensorConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.ISensorLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.nosqljpa.persistence.SensorPersistence;

public class SensorLogic implements ISensorLogic {

	private final SensorPersistence persistence;

	public SensorLogic() {
		this.persistence = new SensorPersistence();
	}

	@Override
	public SensorDTO add(SensorDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		SensorDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public SensorDTO update(SensorDTO dto) {
		SensorDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public SensorDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public SensorDTO findCode(String code) {
		return CONVERTER.entityToDto(persistence.findCode(code));
	}

	@Override
	public List<SensorDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}

}
