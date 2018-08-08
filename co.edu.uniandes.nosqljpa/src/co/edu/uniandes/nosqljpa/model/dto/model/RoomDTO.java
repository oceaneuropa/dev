package co.edu.uniandes.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RoomDTO {

	@Id
	private String id;
	private String name;
	private String code;
	private List<String> consolidatedData;
	private List<String> sensors;

	public RoomDTO() {
		consolidatedData = new ArrayList<String>();
		sensors = new ArrayList<String>();
	}

	public RoomDTO(String id, String name, String code, List<String> consolidatedData, List<String> sensors) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.consolidatedData = consolidatedData;
		this.sensors = sensors;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getConsolidatedData() {
		return consolidatedData;
	}

	public void setConsolidatedData(List<String> consolidatedData) {
		this.consolidatedData = consolidatedData;
	}

	public List<String> getSensors() {
		return sensors;
	}

	public void setSensors(List<String> sensors) {
		this.sensors = sensors;
	}

	public void addConsolidatedData(String id) {
		this.consolidatedData.add(id);
	}

	public void addSensor(String id) {
		this.sensors.add(id);
	}

}
