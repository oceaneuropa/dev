package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.IMeasurementConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.MeasurementDTO;
import co.edu.uniandes.nosqljpa.model.entity.MeasurementEntity;

public class MeasurementConverter implements IMeasurementConverter {

	public static IMeasurementConverter CONVERTER = new MeasurementConverter();

	public MeasurementConverter() {
	}

	@Override
	public MeasurementDTO entityToDto(MeasurementEntity entity) {
		MeasurementDTO dto = new MeasurementDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setUnit(entity.getUnit());
		return dto;
	}

	@Override
	public MeasurementEntity dtoToEntity(MeasurementDTO dto) {
		MeasurementEntity entity = new MeasurementEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setUnit(dto.getUnit());
		return entity;
	}

	@Override
	public List<MeasurementDTO> listEntitiesToListDTOs(List<MeasurementEntity> entities) {
		ArrayList<MeasurementDTO> dtos = new ArrayList<>();
		for (MeasurementEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<MeasurementEntity> listDTOsToListEntities(List<MeasurementDTO> dtos) {
		ArrayList<MeasurementEntity> entities = new ArrayList<>();
		for (MeasurementDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}
