package co.edu.uniandes.nosqljpa.model.dto.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorDTO {

	private String id;
	private String code;
	private List<String> realTimeData;
	private List<String> measurements;

	public SensorDTO() {
		realTimeData = new ArrayList<String>();
		measurements = new ArrayList<String>();
	}

	public SensorDTO(String id, String code, List<String> realTimeData, List<String> measurements) {
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

	public void addMeasurement(String id) {
		this.measurements.add(id);
	}

	public void addRealTimeData(String id) {
		this.realTimeData.add(id);
	}

}
