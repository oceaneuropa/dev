package org.orbit.component.api.tier2.appstore;

import org.origin.common.jdbc.SQLWhereOperator;

public class AppQuery {

	public static String OPERATOR__EQUAL = SQLWhereOperator.EQUAL;
	public static String OPERATOR__NOT_EQUAL = SQLWhereOperator.NOT_EQUAL;
	public static String OPERATOR__LIKE = SQLWhereOperator.LIKE;
	public static String OPERATOR__IN = SQLWhereOperator.IN;

	protected String appId;
	protected String appVersion;
	protected String type;
	protected String name;
	protected String description;

	protected String appId_oper = SQLWhereOperator.EQUAL;
	protected String appVersion_oper = SQLWhereOperator.EQUAL;
	protected String type_oper = SQLWhereOperator.EQUAL;
	protected String name_oper = SQLWhereOperator.LIKE;
	protected String description_oper = SQLWhereOperator.LIKE;

	protected String checkOperator(String operator) {
		if (OPERATOR__EQUAL.equalsIgnoreCase(operator) //
				|| OPERATOR__NOT_EQUAL.equalsIgnoreCase(operator) //
				|| OPERATOR__LIKE.equalsIgnoreCase(operator) //
				|| OPERATOR__IN.equalsIgnoreCase(operator)) {
			return operator;
		}
		return OPERATOR__EQUAL;
	}

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId, String operator) {
		this.appId = appId;
		this.appId_oper = checkOperator(operator);
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String version, String operator) {
		this.appVersion = version;
		this.appVersion_oper = checkOperator(operator);
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type, String operator) {
		this.type = type;
		this.type_oper = checkOperator(operator);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name, String operator) {
		this.name = name;
		this.name_oper = checkOperator(operator);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description, String operator) {
		this.description = description;
		this.description_oper = checkOperator(operator);
	}

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	public String getAppId_oper() {
		return this.appId_oper;
	}

	public String getAppVersion_oper() {
		return this.appVersion_oper;
	}

	public String getType_oper() {
		return this.type_oper;
	}

	public String getName_oper() {
		return this.name_oper;
	}

	public String getDescription_oper() {
		return this.description_oper;
	}

}
