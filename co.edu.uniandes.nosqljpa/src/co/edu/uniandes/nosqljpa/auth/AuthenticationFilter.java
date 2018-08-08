package co.edu.uniandes.nosqljpa.auth;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;

@Secured
@Provider
// @Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	public static final String AUTHENTICATION_SCHEME = "Bearer";

	final JwkProvider provider = new UrlJwkProvider("https://isis2503-fernan.auth0.com/.well-known/jwks.json");
	final String privateKeyId = "PK";
	RSAKeyProvider keyProvider = new RSAKeyProvider() {
		@Override
		public RSAPublicKey getPublicKeyById(String kid) {
			// Received 'kid' value might be null if it wasn't defined in the Token's header
			RSAPublicKey publicKey = null;
			try {
				publicKey = (RSAPublicKey) provider.get(kid).getPublicKey();
			} catch (JwkException ex) {
				Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
			}
			return (RSAPublicKey) publicKey;
		}

		@Override
		public RSAPrivateKey getPrivateKey() {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyPairGenerator keyPairGenerator;
			try {
				keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
				keyPairGenerator.initialize(1024);

				KeyPair keyPair = keyPairGenerator.generateKeyPair();
				return (RSAPrivateKey) keyPair.getPrivate();
			} catch (NoSuchAlgorithmException | NoSuchProviderException ex) {
				Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
			}
			return null;
		}

		@Override
		public String getPrivateKeyId() {
			return privateKeyId;
		}
	};

	Algorithm algorithm = Algorithm.RSA256(keyProvider);

	void verifyToken(String token) {
		try {// Cambiar por variables de entorno
			String issuer = "https://isis2503-fernan.auth0.com/";
			String audience;
			// Access token
			if (!JWT.decode(token).getClaim("gty").isNull() && JWT.decode(token).getClaim("gty").asString().equals("client-credentials")) {
				audience = "uniandes.edu.co/thermalcomfort";
			}
			// ID token
			else {
				audience = "9lRhfqv61bbsblYJ22VkvtuaYOryTrps";
			}
			JWTVerifier verifier = JWT.require(algorithm).withIssuer(issuer).withAudience(audience).build(); // Reusable verifier instance
			verifier.verify(token);
		} catch (JWTVerificationException exception) {
			Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, exception);
			throw exception;
		}
	}

	private boolean isTokenBasedAuthentication(String authorizationHeader) {

		// Check if the Authorization header is valid
		// It must not be null and must be prefixed with "Bearer" plus a whitespace
		// Authentication scheme comparison must be case-insensitive
		return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	private void abortWithUnauthorized(ContainerRequestContext requestContext) {

		// Abort the filter chain with a 401 status code
		// The "WWW-Authenticate" is sent along with the response
		throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME).build());
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Get the Authorization header from the request
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

		// Validate the Authorization header
		if (!isTokenBasedAuthentication(authorizationHeader)) {
			abortWithUnauthorized(requestContext);
		}

		// Extract the token from the Authorization header
		String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();

		try {

			// Validate the token
			verifyToken(token);

		} catch (Exception e) {
			abortWithUnauthorized(requestContext);
		}
	}

}
