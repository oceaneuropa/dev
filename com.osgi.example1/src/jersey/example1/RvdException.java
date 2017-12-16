package jersey.example1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RvdException extends Exception {

	private static final long serialVersionUID = -2430572698886826935L;

	public RvdException() {
		super();
	}

	public RvdException(String message, Throwable cause) {
		super(message, cause);
	}

	public RvdException(String message) {
		super(message);
	}

	public ExceptionResult getExceptionSummary() {
		return new ExceptionResult(getClass().getSimpleName(), getMessage());
	}

	public String asJson() {
		Gson gson = new Gson();
		JsonObject errorResponse = new JsonObject();
		ExceptionResult result = new ExceptionResult(getClass().getSimpleName(), getMessage());
		errorResponse.add("serverError", gson.toJsonTree(result));
		return gson.toJson(errorResponse);
	}

}
