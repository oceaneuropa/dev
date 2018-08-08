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
import co.edu.uniandes.nosqljpa.interfaces.IMeasurementLogic;
import co.edu.uniandes.nosqljpa.interfaces.IRealTimeDataLogic;
import co.edu.uniandes.nosqljpa.interfaces.ISensorLogic;
import co.edu.uniandes.nosqljpa.logic.MeasurementLogic;
import co.edu.uniandes.nosqljpa.logic.RealTimeDataLogic;
import co.edu.uniandes.nosqljpa.logic.SensorLogic;
import co.edu.uniandes.nosqljpa.model.dto.model.MeasurementDTO;
import co.edu.uniandes.nosqljpa.model.dto.model.RealTimeDataDTO;
import co.edu.uniandes.nosqljpa.model.dto.model.SensorDTO;

@Path("/sensors")
@Secured({ Role.admin })
@Produces(MediaType.APPLICATION_JSON)
public class SensorService {

	private final ISensorLogic sensorLogic;
	private final IRealTimeDataLogic realtimedataLogic;
	private final IMeasurementLogic measurementLogic;

	public SensorService() {
		this.sensorLogic = new SensorLogic();
		this.realtimedataLogic = new RealTimeDataLogic();
		this.measurementLogic = new MeasurementLogic();
	}

	@POST
	public SensorDTO add(SensorDTO dto) {
		return sensorLogic.add(dto);
	}

	@POST
	@Secured({ Role.admin, Role.service })
	@Path("{code}/realtimedata")
	public RealTimeDataDTO addRealTimeData(@PathParam("code") String code, RealTimeDataDTO dto) {
		SensorDTO sensor = sensorLogic.findCode(code);
		dto.setIdSensor(sensor.getId());
		RealTimeDataDTO result = realtimedataLogic.add(dto);
		sensor.addRealTimeData(dto.getId());
		sensorLogic.update(sensor);
		return result;
	}

	@GET
	@Secured
	@Path("{code}/realtimedata")
	public List<RealTimeDataDTO> getRealTimeData(@PathParam("code") String code) {
		SensorDTO sensor = sensorLogic.findCode(code);
		return realtimedataLogic.findBySensorId(sensor.getId());
	}

	@POST
	@Path("{code}/measurements")
	public MeasurementDTO addMeasurement(@PathParam("code") String code, MeasurementDTO dto) {
		SensorDTO sensor = sensorLogic.findCode(code);
		MeasurementDTO result = measurementLogic.add(dto);
		sensor.addMeasurement(dto.getId());
		sensorLogic.update(sensor);
		return result;
	}

	@PUT
	public SensorDTO update(SensorDTO dto) {
		return sensorLogic.update(dto);
	}

	@GET
	@Path("/{id}")
	public SensorDTO find(@PathParam("id") String id) {
		return sensorLogic.find(id);
	}

	@GET
	public List<SensorDTO> all() {
		return sensorLogic.all();
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		try {
			sensorLogic.delete(id);
			return Response.status(200).header("Access-Control-Allow-Origin", "*").entity("Sucessful: Sensor was deleted").build();
		} catch (Exception e) {
			Logger.getLogger(SensorService.class.getName()).log(Level.WARNING, e.getMessage());
			return Response.status(500).header("Access-Control-Allow-Origin", "*").entity("We found errors in your query, please contact the Web Admin.").build();
		}
	}

}
