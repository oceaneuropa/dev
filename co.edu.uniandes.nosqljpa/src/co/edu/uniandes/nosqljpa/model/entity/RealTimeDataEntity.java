package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "REALTIMEDATA")
public class RealTimeDataEntity implements Serializable {

	private static final long serialVersionUID = 2603561099077568539L;

	@Id
	private String id;

	@Temporal(TemporalType.DATE)
	private Date samplingTime;

	private double dataValue;

	private String idSensor;

	public RealTimeDataEntity() {
	}

	public RealTimeDataEntity(String id, Date date, double dataValue, String idSensor) {
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
