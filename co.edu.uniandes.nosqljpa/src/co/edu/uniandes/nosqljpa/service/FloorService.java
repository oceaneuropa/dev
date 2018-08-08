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
import co.edu.uniandes.nosqljpa.interfaces.IFloorLogic;
import co.edu.uniandes.nosqljpa.interfaces.IRoomLogic;
import co.edu.uniandes.nosqljpa.logic.FloorLogic;
import co.edu.uniandes.nosqljpa.logic.RoomLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.FloorDTO;
import co.edu.uniandes.nosqljpa.model.dto.model.RoomDTO;

@Path("/floors")
@Secured({ Role.admin })
@Produces(MediaType.APPLICATION_JSON)
public class FloorService {

	private final IFloorLogic floorLogic;
	private final IRoomLogic roomLogic;

	public FloorService() {
		this.floorLogic = new FloorLogic();
		this.roomLogic = new RoomLogic();
	}

	@POST
	public FloorDTO add(FloorDTO dto) {
		return floorLogic.add(dto);
	}

	@POST
	@Path("{code}/rooms")
	public RoomDTO addRoom(@PathParam("code") String code, RoomDTO dto) {
		FloorDTO floor = floorLogic.findCode(code);
		RoomDTO result = roomLogic.add(dto);
		floor.addRoom(dto.getId());
		floorLogic.update(floor);
		return result;
	}

	@PUT
	public FloorDTO update(FloorDTO dto) {
		return floorLogic.update(dto);
	}

	@GET
	@Path("/{id}")
	public FloorDTO find(@PathParam("id") String id) {
		return floorLogic.find(id);
	}

	@GET
	public List<FloorDTO> all() {
		return floorLogic.all();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			floorLogic.delete(id);
			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Floor was deleted").build();
		} catch (Exception e) {
			Logger.getLogger(FloorService.class.getName()).log(Level.WARNING, e.getMessage());
			return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
		}
	}

}
