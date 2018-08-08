package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.ConsolidatedDataConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.IConsolidatedDataLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.ConsolidatedDataDTO;
import co.edu.uniandes.nosqljpa.persistence.ConsolidatedDataPersistence;

public class ConsolidatedDataLogic implements IConsolidatedDataLogic {

	private final ConsolidatedDataPersistence persistence;

	public ConsolidatedDataLogic() {
		this.persistence = new ConsolidatedDataPersistence();
	}

	@Override
	public ConsolidatedDataDTO add(ConsolidatedDataDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		ConsolidatedDataDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public ConsolidatedDataDTO update(ConsolidatedDataDTO dto) {
		ConsolidatedDataDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public List<ConsolidatedDataDTO> findByRoomId(String id) {
		return CONVERTER.listEntitiesToListDTOs(persistence.findByRoomId(id));
	}

	@Override
	public ConsolidatedDataDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public List<ConsolidatedDataDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}

}
