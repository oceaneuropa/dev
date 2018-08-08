package co.edu.uniandes.nosqljpa.model.dto.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConsolidatedDataDTO {

	private String id;
	private double dataValue;
	private Date dateInit;
	private Date dateEnd;
	private String measurementID;
	private String roomID;

	public ConsolidatedDataDTO() {
	}

	public ConsolidatedDataDTO(String id, double dataValue, Date dateInit, Date dateEnd, String measurementID, String roomID) {
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
