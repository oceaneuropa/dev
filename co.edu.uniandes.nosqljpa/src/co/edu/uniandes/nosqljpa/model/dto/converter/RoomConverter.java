package co.edu.uniandes.nosqljpa.model.dto.converter;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.nosqljpa.interfaces.IRoomConverter;
import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;
import co.edu.uniandes.nosqljpa.model.entity.RoomEntity;

public class RoomConverter implements IRoomConverter {

	public static IRoomConverter CONVERTER = new RoomConverter();

	public RoomConverter() {
	}

	@Override
	public RoomDTO entityToDto(RoomEntity entity) {
		RoomDTO dto = new RoomDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setCode(entity.getCode());
		dto.setConsolidatedData(entity.getConsolidatedData());
		dto.setSensors(entity.getSensors());
		return dto;
	}

	@Override
	public RoomEntity dtoToEntity(RoomDTO dto) {
		RoomEntity entity = new RoomEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setCode(dto.getCode());
		entity.setSensors(dto.getSensors());
		entity.setConsolidatedData(dto.getConsolidatedData());
		return entity;
	}

	@Override
	public List<RoomDTO> listEntitiesToListDTOs(List<RoomEntity> entities) {
		ArrayList<RoomDTO> dtos = new ArrayList<>();
		for (RoomEntity entity : entities) {
			dtos.add(entityToDto(entity));
		}
		return dtos;
	}

	@Override
	public List<RoomEntity> listDTOsToListEntities(List<RoomDTO> dtos) {
		ArrayList<RoomEntity> entities = new ArrayList<>();
		for (RoomDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}
		return entities;
	}

}
