package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.FloorDTO;
import co.edu.uniandes.nosqljpa.model.entity.FloorEntity;

public interface IFloorConverter {

	public FloorDTO entityToDto(FloorEntity entity);

	public FloorEntity dtoToEntity(FloorDTO dto);

	public List<FloorDTO> listEntitiesToListDTOs(List<FloorEntity> entities);

	public List<FloorEntity> listDTOsToListEntities(List<FloorDTO> dtos);

}
