package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.ISensorConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.nosqljpa.model.entity.SensorEntity;

public class SensorConverter implements ISensorConverter {

	public static ISensorConverter CONVERTER = new SensorConverter();

	public SensorConverter() {
	}

	@Override
	public SensorDTO entityToDto(SensorEntity entity) {
		SensorDTO dto = new SensorDTO();
		dto.setId(entity.getId());
		dto.setCode(entity.getCode());
		dto.setMeasurements(entity.getMeasurements());
		dto.setRealTimeData(entity.getRealTimeData());
		return dto;
	}

	@Override
	public SensorEntity dtoToEntity(SensorDTO dto) {
		SensorEntity entity = new SensorEntity();
		entity.setId(dto.getId());
		entity.setCode(dto.getCode());
		entity.setMeasurements(dto.getMeasurements());
		entity.setRealTimeData(dto.getRealTimeData());
		return entity;
	}

	@Override
	public List<SensorDTO> listEntitiesToListDTOs(List<SensorEntity> entities) {
		ArrayList<SensorDTO> dtos = new ArrayList<>();
		for (SensorEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<SensorEntity> listDTOsToListEntities(List<SensorDTO> dtos) {
		ArrayList<SensorEntity> entities = new ArrayList<>();
		for (SensorDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}
