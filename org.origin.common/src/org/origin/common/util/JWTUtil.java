package org.origin.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTUtil {

	/**
	 * 
	 * @param secret
	 * @param issuer
	 * @param subject
	 * @param audiences
	 * @param expiresAt
	 * @param keyValuePairs
	 * @return
	 */
	public static String createToken_HMAC256(String secret, String issuer, String subject, String audiences, Date expiresAt, String[]... keyValuePairs) {
		String token = null;
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			Builder builder = JWT.create();
			builder.withIssuer(issuer);
			builder.withSubject(subject);
			builder.withAudience(audiences);
			builder.withExpiresAt(expiresAt);
			builder.withIssuedAt(new Date());

			for (String[] keyValuePair : keyValuePairs) {
				builder.withClaim(keyValuePair[0], keyValuePair[1]);
			}
			token = builder.sign(algorithm);

		} catch (UnsupportedEncodingException exception) {
			// UTF-8 encoding not supported
			exception.printStackTrace();

		} catch (JWTCreationException exception) {
			// Invalid Signing configuration / Couldn't convert Claims.
			exception.printStackTrace();
		}
		return token;
	}

	/**
	 * 
	 * @param secret
	 * @param issuer
	 * @param token
	 */
	public static void verifyToken_HMAC256(String secret, String issuer, String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).build(); // Reusable verifier instance
			DecodedJWT jwt = verifier.verify(token);

			System.out.println("Verified JWT:\r\n" + jwt);
			if (jwt != null) {
				String jwtId = jwt.getId();
				String jwtKeyId = jwt.getKeyId();
				String jwtAlgorithm = jwt.getAlgorithm();
				String jwtIssuer = jwt.getIssuer();

				String jwtToken = jwt.getToken();
				String jwtHeader = jwt.getHeader();
				String jwtPayload = jwt.getPayload();
				String jwtSignature = jwt.getSignature();

				String jwtType = jwt.getType();
				String jwtContentType = jwt.getContentType();
				String jwtSubject = jwt.getSubject();
				Date jwtExpires = jwt.getExpiresAt();

				List<String> audiences = jwt.getAudience();
				Map<String, Claim> claimsMap = jwt.getClaims();

				System.out.println("\tid: " + jwtId);
				System.out.println("\tkeyId: " + jwtKeyId);
				System.out.println("\talgorithm: " + jwtAlgorithm);
				System.out.println("\tissuer: " + jwtIssuer);
				System.out.println("\ttoken: " + jwtToken);
				System.out.println("\theader: " + jwtHeader);
				System.out.println("\tpayload: " + jwtPayload);
				System.out.println("\tsignature: " + jwtSignature);
				System.out.println("\ttype: " + jwtType);
				System.out.println("\tcontentType: " + jwtContentType);
				System.out.println("\tsubject: " + jwtSubject);
				System.out.println("\texpires: " + jwtExpires);

				if (audiences != null) {
					System.out.println("\taudiences (size=" + audiences.size() + "): ");
					for (String audience : audiences) {
						System.out.println("\t\t" + audience);
					}
				} else {
					System.out.println("\taudiences is null");
				}

				if (claimsMap != null) {
					System.out.println("\tclaims (size=" + claimsMap.size() + "): ");
					for (Iterator<String> claimItor = claimsMap.keySet().iterator(); claimItor.hasNext();) {
						String key = claimItor.next();
						Claim claim = claimsMap.get(key);
						System.out.println("\t\t" + key + " = " + claim.asString());
					}
				} else {
					System.out.println("\tclaims is null");
				}

			} else {
				System.out.println("\tjwt is null");
			}

		} catch (UnsupportedEncodingException exception) {
			// UTF-8 encoding not supported
			exception.printStackTrace();
		} catch (JWTVerificationException exception) {
			// Invalid signature/claims
			exception.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Date expiresAt = DateUtil.addMinutes(new Date(), 30);

		String jwt = JWTUtil.createToken_HMAC256("secret1", "auth1", "subject", "audiences", expiresAt, //
				new String[] { "username", "tomhanks123" }, //
				new String[] { "email", "tomhanks@tom.com" }, //
				new String[] { "firstName", "Tom" }, //
				new String[] { "lastName", "Hanks" } //
		);
		System.out.println("Created token1:\r\n" + jwt);
		System.out.println();

		verifyToken_HMAC256("secret1", "auth1", jwt);
	}

}
