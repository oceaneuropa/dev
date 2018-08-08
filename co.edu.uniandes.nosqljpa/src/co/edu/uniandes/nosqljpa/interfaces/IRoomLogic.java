package co.edu.uniandes.nosqljpa.interfaces;

import java.util.List;

import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;

public interface IRoomLogic {

	public RoomDTO add(RoomDTO dto);

	public RoomDTO update(RoomDTO dto);

	public RoomDTO find(String id);

	public RoomDTO findCode(String code);

	public List<RoomDTO> all();

	public Boolean delete(String id);

}
