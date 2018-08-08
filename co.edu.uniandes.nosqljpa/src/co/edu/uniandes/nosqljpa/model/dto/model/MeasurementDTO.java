package co.edu.uniandes.nosqljpa.model.dto.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasurementDTO {

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
