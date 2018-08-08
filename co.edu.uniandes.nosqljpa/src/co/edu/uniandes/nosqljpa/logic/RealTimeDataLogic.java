package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.RealTimeDataConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.IRealTimeDataLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.RealTimeDataDTO;
import co.edu.uniandes.nosqljpa.persistence.RealTimeDataPersistence;

public class RealTimeDataLogic implements IRealTimeDataLogic {

	private final RealTimeDataPersistence persistence;

	public RealTimeDataLogic() {
		this.persistence = new RealTimeDataPersistence();
	}

	@Override
	public RealTimeDataDTO add(RealTimeDataDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		RealTimeDataDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public RealTimeDataDTO update(RealTimeDataDTO dto) {
		RealTimeDataDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public RealTimeDataDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public List<RealTimeDataDTO> findBySensorId(String id) {
		return CONVERTER.listEntitiesToListDTOs(persistence.findBySensorId(id));
	}

	@Override
	public List<RealTimeDataDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}

}
