package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROOM")
public class RoomEntity implements Serializable {

	private static final long serialVersionUID = 8545298796409924443L;

	@Id
	private String id;

	private String name;

	private String code;

	@ElementCollection
	private List<String> consolidatedData;

	@ElementCollection
	private List<String> sensors;

	public RoomEntity() {
		consolidatedData = new ArrayList<String>();
		sensors = new ArrayList<String>();
	}

	public RoomEntity(String id, String name, String code, List<String> consolidatedData, List<String> sensors) {
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

}
