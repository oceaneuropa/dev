package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.ConsolidatedDataDTO;
import co.edu.uniandes.nosqljpa.model.entity.ConsolidatedDataEntity;

public interface IConsolidatedDataConverter {

	public ConsolidatedDataDTO entityToDto(ConsolidatedDataEntity entity);

	public ConsolidatedDataEntity dtoToEntity(ConsolidatedDataDTO dto);

	public List<ConsolidatedDataDTO> listEntitiesToListDTOs(List<ConsolidatedDataEntity> entities);

	public List<ConsolidatedDataEntity> listDTOsToListEntities(List<ConsolidatedDataDTO> dtos);

}
