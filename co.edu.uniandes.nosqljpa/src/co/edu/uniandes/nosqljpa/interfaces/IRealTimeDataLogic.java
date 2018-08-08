package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.RealTimeDataDTO;

public interface IRealTimeDataLogic {

	public RealTimeDataDTO add(RealTimeDataDTO dto);

	public RealTimeDataDTO update(RealTimeDataDTO dto);

	public RealTimeDataDTO find(String id);

	public List<RealTimeDataDTO> findBySensorId(String id);

	public List<RealTimeDataDTO> all();

	public Boolean delete(String id);

}
