package co.edu.uniandes.nosqljpa.model.dto.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RealTimeDataDTO {

	private String id;
	private Date samplingTime;
	private double dataValue;
	private String idSensor;

	public RealTimeDataDTO() {
	}

	public RealTimeDataDTO(String id, Date date, double dataValue, String idSensor) {
		this.id = id;
		this.samplingTime = date;
		this.dataValue = dataValue;
		this.idSensor = idSensor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getSamplingTime() {
		return samplingTime;
	}

	public void setSamplingTime(Date samplingTime) {
		this.samplingTime = samplingTime;
	}

	public double getDataValue() {
		return dataValue;
	}

	public void setDataValue(double dataValue) {
		this.dataValue = dataValue;
	}

	public String getIdSensor() {
		return idSensor;
	}

	public void setIdSensor(String idSensor) {
		this.idSensor = idSensor;
	}

}
