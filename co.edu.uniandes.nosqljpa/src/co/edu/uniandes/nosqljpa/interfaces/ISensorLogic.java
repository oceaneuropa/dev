package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;

public interface ISensorLogic {

	public SensorDTO add(SensorDTO dto);

	public SensorDTO update(SensorDTO dto);

	public SensorDTO find(String id);

	public SensorDTO findCode(String code);

	public List<SensorDTO> all();

	public Boolean delete(String id);

}
