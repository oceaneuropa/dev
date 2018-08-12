package org.orbit.component.runtime.common.token;

import java.util.Date;

import org.orbit.component.runtime.model.account.UserAccount;
import org.orbit.platform.sdk.http.OrbitRoles;
import org.origin.common.rest.annotation.Secured;
import org.origin.common.util.DateUtil;
import org.origin.common.util.JWTUtil;

public class TokenUtil {

	public static final String AUTHENTICATION_SCHEME = "Bearer";

	/**
	 * 
	 * @param tokenSecret
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	public static String createAccessToken(String tokenSecret, UserAccount userAccount) throws Exception {
		// Some attributes related to JWT token itself.
		// see https://tools.ietf.org/html/rfc7519#section-4.1.3
		// see https://stackoverflow.com/questions/28418360/jwt-json-web-token-audience-aud-versus-client-id-whats-the-difference
		// String issuer = "orbit.identity_service";
		// String subject = "orbit.identity";
		// use "orbit.runtime.access" for creating "access" token.
		// use "orbit.runtime.refresh" for creating "refresh" token.
		// String audiences = "orbit.runtime.access";
		String issuer = "orbit.identity_service";
		String subject = "orbit.identity";
		String audiences = "orbit.runtime.access";

		String accessToken = createAccessToken(issuer, subject, audiences, tokenSecret, userAccount);
		return accessToken;
	}

	/**
	 * 
	 * @param issuer
	 * @param subject
	 * @param audiences
	 * @param tokenSecret
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	public static String createAccessToken(String issuer, String subject, String audiences, String tokenSecret, UserAccount userAccount) throws Exception {
		Date now = new Date();
		Date accessTokenExpiresAt = DateUtil.addMinutes(now, 30);

		// Get user basic information
		String username = userAccount.getUserId();
		String email = userAccount.getEmail();
		String firstName = userAccount.getFirstName();
		String lastName = userAccount.getLastName();

		// Need to get roles, securityLevel and classificationLevels associated with UserAccount.
		String roles = OrbitRoles.SYSTEM_ADMIN + "," + OrbitRoles.USER;
		int securityLevel = Secured.SecurityLevels.LEVEL_1;
		String classificationLevels = Secured.ClassificationLevels.TOP_SECRET + "," + Secured.ClassificationLevels.SECRET + "," + Secured.ClassificationLevels.CONFIDENTIAL;

		// Key value pairs to be stored in JWT token.
		String[][] userProfileKeyValuePairs = new String[][] { //
				new String[] { "username", username }, //
				new String[] { "email", email }, //
				new String[] { "firstName", firstName }, //
				new String[] { "lastName", lastName }, //
				new String[] { "roles", roles }, //
				new String[] { "securityLevel", String.valueOf(securityLevel) }, //
				new String[] { "classificationLevels", classificationLevels } //
		};

		String accessToken = JWTUtil.createToken(tokenSecret, issuer, subject, audiences, accessTokenExpiresAt, userProfileKeyValuePairs);
		return accessToken;
	}

	/**
	 * 
	 * @param userAccount
	 * @return
	 * @throws Exception
	 */
	public static UserToken createAccessTokenObject(String tokenSecret, UserAccount userAccount) throws Exception {
		UserToken userToken = null;
		try {
			// String issuer = getFullName();
			String issuer = "orbit";
			String subject1 = "user_access_token";
			String subject2 = "user_refresh_token";

			// https://stackoverflow.com/questions/28418360/jwt-json-web-token-audience-aud-versus-client-id-whats-the-difference
			// The audience of a token is the intended recipient of the token.
			// The audience value is a string -- typically, the base address of the resource being accessed, such as "https://contoso.com".
			String audiences = userAccount.getUserId();

			// Access token expires in 30 minutes
			// Refresh token expires in 24 hours
			// - The access token should be updated/refreshed with each access to web services. Accessing web services should return response header for
			// the new
			// access token with updated expiration time.
			Date now = new Date();
			Date accessTokenExpiresAt = DateUtil.addMinutes(now, 30);
			Date refreshTokenExpiresAt = DateUtil.addHours(now, 24);

			String username = userAccount.getUserId();
			String email = userAccount.getEmail();
			String firstName = userAccount.getFirstName();
			String lastName = userAccount.getLastName();
			String[][] keyValuePairs = new String[][] { new String[] { "username", username }, new String[] { "email", email }, new String[] { "firstName", firstName }, new String[] { "lastName", lastName } };

			String accessToken = JWTUtil.createToken(tokenSecret, issuer, subject1, audiences, accessTokenExpiresAt, keyValuePairs);
			String refreshToken = JWTUtil.createToken(tokenSecret, issuer, subject2, audiences, refreshTokenExpiresAt, keyValuePairs);

			userToken = new UserToken();
			userToken.setUsername(username);
			userToken.setAccessToken(accessToken);
			userToken.setRefreshToken(refreshToken);
			userToken.setAccessTokenExpireTime(accessTokenExpiresAt);
			userToken.setRefreshTokenExpireTime(refreshTokenExpiresAt);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return userToken;
	}

}
