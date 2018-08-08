package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SENSOR")
public class SensorEntity implements Serializable {

	private static final long serialVersionUID = 1702205579477344334L;

	@Id
	private String id;

	private String code;

	@ElementCollection
	private List<String> realTimeData;

	@ElementCollection
	private List<String> measurements;

	public SensorEntity() {
		realTimeData = new ArrayList<String>();
		measurements = new ArrayList<String>();
	}

	public SensorEntity(String id, String code, List<String> realTimeData, List<String> measurements) {
		this.id = id;
		this.code = code;
		this.realTimeData = realTimeData;
		this.measurements = measurements;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getRealTimeData() {
		return realTimeData;
	}

	public void setRealTimeData(List<String> realTimeData) {
		this.realTimeData = realTimeData;
	}

	public List<String> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<String> measurements) {
		this.measurements = measurements;
	}

}
