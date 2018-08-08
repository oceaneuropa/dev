package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.MeasurementDTO;
import co.edu.uniandes.nosqljpa.model.entity.MeasurementEntity;

public interface IMeasurementConverter {

	public MeasurementDTO entityToDto(MeasurementEntity entity);

	public MeasurementEntity dtoToEntity(MeasurementDTO dto);

	public List<MeasurementDTO> listEntitiesToListDTOs(List<MeasurementEntity> entities);

	public List<MeasurementEntity> listDTOsToListEntities(List<MeasurementDTO> dtos);

}
