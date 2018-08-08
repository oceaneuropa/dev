package co.edu.uniandes.nosqljpa.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.edu.uniandes.nosqljpa.auth.AuthorizationFilter.Role;
import co.edu.uniandes.nosqljpa.auth.Secured;
import co.edu.uniandes.nosqljpa.interfaces.IConsolidatedDataLogic;
import co.edu.uniandes.nosqljpa.interfaces.IRoomLogic;
import co.edu.uniandes.nosqljpa.interfaces.ISensorLogic;
import co.edu.uniandes.nosqljpa.logic.ConsolidatedDataLogic;
import co.edu.uniandes.nosqljpa.logic.RoomLogic;
import co.edu.uniandes.nosqljpa.logic.SensorLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.ConsolidatedDataDTO;
import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;
import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;

@Path("/rooms")
@Secured({ Role.admin })
@Produces(MediaType.APPLICATION_JSON)
public class RoomService {

	private final IRoomLogic roomLogic;
	private final IConsolidatedDataLogic consolidateddataLogic;
	private final ISensorLogic sensorLogic;

	public RoomService() {
		this.roomLogic = new RoomLogic();
		this.consolidateddataLogic = new ConsolidatedDataLogic();
		this.sensorLogic = new SensorLogic();
	}

	@POST
	public RoomDTO add(RoomDTO dto) {
		return roomLogic.add(dto);
	}

	@POST
	@Secured({ Role.admin })
	@Path("{code}/consolidateddata")
	public ConsolidatedDataDTO addConsolidatedData(@PathParam("code") String code, ConsolidatedDataDTO dto) {
		RoomDTO room = roomLogic.findCode(code);
		// Find the id of the measurement associated with the first sensor on the room
		dto.setMeasurementID(sensorLogic.find(room.getSensors().get(0)).getMeasurements().get(0));
		dto.setRoomID(room.getId());
		ConsolidatedDataDTO result = consolidateddataLogic.add(dto);
		room.addConsolidatedData(dto.getId());
		roomLogic.update(room);
		return result;
	}

	@GET
	@Secured
	@Path("{code}/consolidateddata")
	public List<ConsolidatedDataDTO> getConsolidatedData(@PathParam("code") String code) {
		RoomDTO room = roomLogic.findCode(code);
		return consolidateddataLogic.findByRoomId(room.getId());
	}

	@POST
	@Path("{code}/sensors")
	public SensorDTO addSensor(@PathParam("code") String code, SensorDTO dto) {
		RoomDTO room = roomLogic.findCode(code);
		SensorDTO result = sensorLogic.add(dto);
		room.addSensor(dto.getId());
		roomLogic.update(room);
		return result;
	}

	@PUT
	public RoomDTO update(RoomDTO dto) {
		return roomLogic.update(dto);
	}

	@GET
	@Path("/{id}")
	public RoomDTO find(@PathParam("id") String id) {
		return roomLogic.find(id);
	}

	@GET
	public List<RoomDTO> all() {
		return roomLogic.all();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			roomLogic.delete(id);
			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Room was deleted").build();
		} catch (Exception e) {
			Logger.getLogger(RoomService.class.getName()).log(Level.WARNING, e.getMessage());
			return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
		}
	}

}
