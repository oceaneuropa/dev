package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.IConsolidatedDataConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.ConsolidatedDataDTO;
import co.edu.uniandes.nosqljpa.model.entity.ConsolidatedDataEntity;

public class ConsolidatedDataConverter implements IConsolidatedDataConverter {

	public static IConsolidatedDataConverter CONVERTER = new ConsolidatedDataConverter();

	public ConsolidatedDataConverter() {
	}

	@Override
	public ConsolidatedDataDTO entityToDto(ConsolidatedDataEntity entity) {
		ConsolidatedDataDTO dto = new ConsolidatedDataDTO();
		dto.setId(entity.getId());
		dto.setDataValue(entity.getDataValue());
		dto.setDateInit(entity.getDateInit());
		dto.setDateEnd(entity.getDateEnd());
		dto.setMeasurementID(entity.getMeasurementID());
		dto.setRoomID(entity.getRoomID());
		return dto;
	}

	@Override
	public ConsolidatedDataEntity dtoToEntity(ConsolidatedDataDTO dto) {
		ConsolidatedDataEntity entity = new ConsolidatedDataEntity();
		entity.setId(dto.getId());
		entity.setDataValue(dto.getDataValue());
		entity.setDateInit(dto.getDateInit());
		entity.setDateEnd(dto.getDateEnd());
		entity.setMeasurementID(dto.getMeasurementID());
		entity.setRoomID(dto.getRoomID());
		return entity;
	}

	@Override
	public List<ConsolidatedDataDTO> listEntitiesToListDTOs(List<ConsolidatedDataEntity> entities) {
		ArrayList<ConsolidatedDataDTO> dtos = new ArrayList<>();
		for (ConsolidatedDataEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<ConsolidatedDataEntity> listDTOsToListEntities(List<ConsolidatedDataDTO> dtos) {
		ArrayList<ConsolidatedDataEntity> entities = new ArrayList<>();
		for (ConsolidatedDataDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}
