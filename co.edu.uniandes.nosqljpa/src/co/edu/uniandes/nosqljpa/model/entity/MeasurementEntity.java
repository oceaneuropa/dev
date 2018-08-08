package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEASUREMENT")
public class MeasurementEntity implements Serializable {

	private static final long serialVersionUID = 719425681051371470L;

	@Id
	private String id;

	private String name;

	private String unit;

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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
