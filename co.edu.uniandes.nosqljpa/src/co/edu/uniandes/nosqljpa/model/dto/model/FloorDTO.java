package co.edu.uniandes.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FloorDTO {

	private String id;
	private String name;
	private String code;
	private List<String> rooms;

	public FloorDTO() {
		this.rooms = new ArrayList<String>();
	}

	public FloorDTO(String id, String name, String code, List<String> rooms) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.rooms = rooms;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getRooms() {
		return rooms;
	}

	public void setRooms(List<String> rooms) {
		this.rooms = rooms;
	}

	public void addRoom(String id) {
		this.rooms.add(id);
	}
}
