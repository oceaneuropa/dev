package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.MeasurementDTO;

public interface IMeasurementLogic {

	public MeasurementDTO add(MeasurementDTO dto);

	public MeasurementDTO update(MeasurementDTO dto);

	public MeasurementDTO find(String id);

	public List<MeasurementDTO> all();

	public Boolean delete(String id);

}
