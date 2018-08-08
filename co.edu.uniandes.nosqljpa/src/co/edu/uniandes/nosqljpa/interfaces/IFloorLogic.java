package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.FloorDTO;

public interface IFloorLogic {

	public FloorDTO add(FloorDTO dto);

	public FloorDTO update(FloorDTO dto);

	public FloorDTO find(String id);

	public FloorDTO findCode(String code);

	public List<FloorDTO> all();

	public Boolean delete(String id);

}
