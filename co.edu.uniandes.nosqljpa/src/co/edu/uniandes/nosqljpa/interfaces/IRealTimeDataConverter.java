package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.RealTimeDataDTO;
import co.edu.uniandes.nosqljpa.model.entity.RealTimeDataEntity;

public interface IRealTimeDataConverter {

	public RealTimeDataDTO entityToDto(RealTimeDataEntity entity);

	public RealTimeDataEntity dtoToEntity(RealTimeDataDTO dto);

	public List<RealTimeDataDTO> listEntitiesToListDTOs(List<RealTimeDataEntity> entities);

	public List<RealTimeDataEntity> listDTOsToListEntities(List<RealTimeDataDTO> dtos);

}
