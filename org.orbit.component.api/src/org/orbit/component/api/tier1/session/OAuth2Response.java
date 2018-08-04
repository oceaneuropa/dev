package org.orbit.component.api.tier1.session;

/*
 * doc:
 * https://developer.github.com/v3/oauth/
 * https://www.digitalocean.com/community/tutorials/how-to-use-oauth-authentication-with-digitalocean-as-a-user-or-developer
 * 
 * oauth2 response example:
 * ----------------------------------------------------------------------
 * {
 *     "token_type":"Bearer",
 *     "scope":"repo,gist",
 *     "access_token": "eyJhbGciOiJIUzI1NiIsImtpZCI6Imp3dF91c2VyX2tleSJ9.eyJsYXN0TmFtZSI6IklOQyIsImVtYWlsIjoiZGFwYUBwb3N0b25saW5lLm1lIiwic3ViamVjdCI6IjhBQTE4NzI3LUZDRjktOUNCNC0xMTdCLUI1RTdBQjkyQzM4QyIsImZpcnN0TmFtZSI6IlRJQkNPIiwiZHBsU3RhdHVzIjoiUGFzcyIsImV4cCI6MTQ0NTY1ODI2OSwic2NvcGUiOltdLCJjbGllbnRfaWQiOiJyb3BjX2lwYXNzIn0.q7ZzCp-6y9EzOq4AbiZ_DnrS4WpgsNdAOUB17CPbNtI" // encoded JWT
 *     "expires_in":7199, // ?
 * }
 * ----------------------------------------------------------------------
 * 
 * decoded "access_token" (JWT) example:
 * ----------------------------------------------------------------------
 * {
 *     "client_id": "ropc_ipass" // application id
 *     "firstName": "MyFirstName",
 *     "lastName": "MyLastName",
 *     "email": "dapa@postonline.me",
 *     "subject": "8AA18727-FCF9-9CB4-117B-B5E7AB92C38C", // ?
 *     "dplStatus": "Pass", // ?
 *     "exp": 1445658269,
 *     "scope": [read,write],
 * }
 * ----------------------------------------------------------------------
 *  
 * Example oauth2 response:
 * ----------------------------------------------------------------------
 * {
 *     "token_type":"bearer"
 *     "scope":"repo,gist", 
 *     "access_token":"e72e16c7e42f292c6912e7710c838347ae178b4a", 
 * }
 * ----------------------------------------------------------------------
 * (Accept: application/json)
 * 
 * ----------------------------------------------------------------------
 * <OAuth>
 *     <token_type>bearer</token_type>
 *     <scope>repo,gist</scope>
 *     <access_token>e72e16c7e42f292c6912e7710c838347ae178b4a</access_token>
 * </OAuth>
 * ----------------------------------------------------------------------
 * (Accept: application/xml)
 *
 */
public class OAuth2Response {

	protected String token_type; // e.g. "bearer"
	protected String scope;
	protected String access_token; // encoded JWT

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
