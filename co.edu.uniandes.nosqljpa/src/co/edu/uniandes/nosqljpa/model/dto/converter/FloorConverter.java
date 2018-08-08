package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.IFloorConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.FloorDTO;
import co.edu.uniandes.nosqljpa.model.entity.FloorEntity;

public class FloorConverter implements IFloorConverter {

	public static IFloorConverter CONVERTER = new FloorConverter();

	public FloorConverter() {
	}

	@Override
	public FloorDTO entityToDto(FloorEntity entity) {
		FloorDTO dto = new FloorDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setCode(entity.getCode());
		dto.setRooms(entity.getRooms());
		return dto;
	}

	@Override
	public FloorEntity dtoToEntity(FloorDTO dto) {
		FloorEntity entity = new FloorEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setCode(dto.getCode());
		entity.setRooms(dto.getRooms());
		return entity;
	}

	@Override
	public List<FloorDTO> listEntitiesToListDTOs(List<FloorEntity> entities) {
		ArrayList<FloorDTO> dtos = new ArrayList<>();
		for (FloorEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<FloorEntity> listDTOsToListEntities(List<FloorDTO> dtos) {
		ArrayList<FloorEntity> entities = new ArrayList<>();
		for (FloorDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}