package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.ConsolidatedDataDTO;

public interface IConsolidatedDataLogic {

	public ConsolidatedDataDTO add(ConsolidatedDataDTO dto);

	public ConsolidatedDataDTO update(ConsolidatedDataDTO dto);

	public ConsolidatedDataDTO find(String id);

	public List<ConsolidatedDataDTO> findByRoomId(String id);

	public List<ConsolidatedDataDTO> all();

	public Boolean delete(String id);

}
