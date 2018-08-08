package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FLOOR")
public class FloorEntity implements Serializable {

	private static final long serialVersionUID = 2756836746440610792L;

	@Id
	private String id;

	private String name;

	private String code;

	@ElementCollection
	private List<String> rooms;

	public FloorEntity() {
		this.rooms = new ArrayList<String>();
	}

	public FloorEntity(String id, String name, String code, List<String> rooms) {
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

}
