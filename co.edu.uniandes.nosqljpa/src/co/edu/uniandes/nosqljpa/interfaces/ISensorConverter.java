package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;
import co.edu.uniandes.nosqljpa.model.entity.SensorEntity;

public interface ISensorConverter {

	public SensorDTO entityToDto(SensorEntity entity);

	public SensorEntity dtoToEntity(SensorDTO dto);

	public List<SensorDTO> listEntitiesToListDTOs(List<SensorEntity> entities);

	public List<SensorEntity> listDTOsToListEntities(List<SensorDTO> dtos);

}
