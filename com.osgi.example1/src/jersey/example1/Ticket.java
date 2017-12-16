package jersey.example1;

import java.util.Date;
import java.util.UUID;

public class Ticket {

	private String ticketId;
	private String userId;
	private Date timeCreated;
	private Date timeLastAccessed;
	private String authenticationToken;
	private Boolean cookieBased;

	public Ticket() {
		timeCreated = new Date();
		timeLastAccessed = timeCreated;
	}

	public Ticket(String userId) {
		super();
		this.userId = userId;
		this.ticketId = UUID.randomUUID().toString();
		timeCreated = new Date();
		timeLastAccessed = timeCreated;
	}

	public Ticket(String userId, String ticketId) {
		super();
		this.ticketId = ticketId;
		this.userId = userId;
		timeCreated = new Date();
		timeLastAccessed = timeCreated;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getTimeLastAccessed() {
		return timeLastAccessed;
	}

	public void setTimeLastAccessed(Date timeLastAccessed) {
		this.timeLastAccessed = timeLastAccessed;
	}

	public Boolean getCookieBased() {
		return cookieBased;
	}

	public void setCookieBased(Boolean cookieBased) {
		this.cookieBased = cookieBased;
	}

	@Override
	public String toString() {
		return "[userId: " + userId + ",ticketId: " + ticketId + ",timeCreated: " + timeCreated + ", timeLastAccessed: " + timeLastAccessed + "]";
	}

	public void accessedNow() {
		timeLastAccessed = new Date();
	}

	public String getAuthenticationToken() {
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

}
