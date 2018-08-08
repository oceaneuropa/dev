package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.IRealTimeDataConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.RealTimeDataDTO;
import co.edu.uniandes.nosqljpa.model.entity.RealTimeDataEntity;

public class RealTimeDataConverter implements IRealTimeDataConverter {

	public static IRealTimeDataConverter CONVERTER = new RealTimeDataConverter();

	public RealTimeDataConverter() {
	}

	@Override
	public RealTimeDataDTO entityToDto(RealTimeDataEntity entity) {
		RealTimeDataDTO dto = new RealTimeDataDTO();
		dto.setId(entity.getId());
		dto.setDataValue(entity.getDataValue());
		dto.setIdSensor(entity.getIdSensor());
		dto.setSamplingTime(entity.getSamplingTime());
		return dto;
	}

	@Override
	public RealTimeDataEntity dtoToEntity(RealTimeDataDTO dto) {
		RealTimeDataEntity entity = new RealTimeDataEntity();
		entity.setId(dto.getId());
		entity.setDataValue(dto.getDataValue());
		entity.setIdSensor(dto.getIdSensor());
		entity.setSamplingTime(dto.getSamplingTime());
		return entity;
	}

	@Override
	public List<RealTimeDataDTO> listEntitiesToListDTOs(List<RealTimeDataEntity> entities) {
		ArrayList<RealTimeDataDTO> dtos = new ArrayList<>();
		for (RealTimeDataEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<RealTimeDataEntity> listDTOsToListEntities(List<RealTimeDataDTO> dtos) {
		ArrayList<RealTimeDataEntity> entities = new ArrayList<>();
		for (RealTimeDataDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}
