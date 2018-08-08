package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;
import co.edu.uniandes.nosqljpa.model.entity.RoomEntity;

public interface IRoomConverter {

	public RoomDTO entityToDto(RoomEntity entity);

	public RoomEntity dtoToEntity(RoomDTO dto);

	public List<RoomDTO> listEntitiesToListDTOs(List<RoomEntity> entities);

	public List<RoomEntity> listDTOsToListEntities(List<RoomDTO> dtos);

}
