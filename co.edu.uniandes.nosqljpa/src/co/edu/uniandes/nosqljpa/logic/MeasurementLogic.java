package co.edu.uniandes.nosqljpa.logic;

import static co.edu.uniandes.nosqljpa.model.dto.converter.MeasurementConverter.CONVERTER;

import java.util.List;
import java.util.UUID;

import co.edu.uniandes.nosqljpa.interfaces.IMeasurementLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.MeasurementDTO;
import co.edu.uniandes.nosqljpa.persistence.MeasurementPersistence;

public class MeasurementLogic implements IMeasurementLogic {

	private final MeasurementPersistence persistence;

	public MeasurementLogic() {
		this.persistence = new MeasurementPersistence();
	}

	@Override
	public MeasurementDTO add(MeasurementDTO dto) {
		if (dto.getId() == null) {
			dto.setId(UUID.randomUUID().toString());
		}
		MeasurementDTO result = CONVERTER.entityToDto(persistence.add(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public MeasurementDTO update(MeasurementDTO dto) {
		MeasurementDTO result = CONVERTER.entityToDto(persistence.update(CONVERTER.dtoToEntity(dto)));
		return result;
	}

	@Override
	public MeasurementDTO find(String id) {
		return CONVERTER.entityToDto(persistence.find(id));
	}

	@Override
	public List<MeasurementDTO> all() {
		return CONVERTER.listEntitiesToListDTOs(persistence.all());
	}

	@Override
	public Boolean delete(String id) {
		return persistence.delete(id);
	}

}
