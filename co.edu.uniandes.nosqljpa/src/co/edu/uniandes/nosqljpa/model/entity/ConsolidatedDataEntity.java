package co.edu.uniandes.nosqljpa.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CONSOLIDATED_DATA")
public class ConsolidatedDataEntity implements Serializable {

	private static final long serialVersionUID = 7395169664380978814L;

	@Id
	private String id;

	private double dataValue;

	@Temporal(TemporalType.DATE)
	private Date dateInit;

	@Temporal(TemporalType.DATE)
	private Date dateEnd;

	private String measurementID;

	private String roomID;

	public ConsolidatedDataEntity() {
	}

	public ConsolidatedDataEntity(String id, double dataValue, Date dateInit, Date dateEnd, String measurementID, String roomID) {
		this.id = id;
		this.dataValue = dataValue;
		this.dateInit = dateInit;
		this.dateEnd = dateEnd;
		this.measurementID = measurementID;
		this.roomID = roomID;
	}

	public String getRoomID() {
		return roomID;
	}

	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	public String getMeasurementID() {
		return measurementID;
	}

	public void setMeasurementID(String measurementID) {
		this.measurementID = measurementID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getDataValue() {
		return dataValue;
	}

	public void setDataValue(double dataValue) {
		this.dataValue = dataValue;
	}

	public Date getDateInit() {
		return dateInit;
	}

	public void setDateInit(Date dateInit) {
		this.dateInit = dateInit;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

}
