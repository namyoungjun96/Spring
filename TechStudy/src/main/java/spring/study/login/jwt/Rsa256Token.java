package spring.study.login.jwt;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class Rsa256Token implements TokenHandler {
	public void createToken() {
		String accesssValue = "ehdgornltls";
		
		JWTCreator.Builder builder = JWT.create();
		builder.withClaim("access_key", accesssValue);
		// 아직 뭔지 모르겠다. JWT는 Claim이라는 값을 통해서 헤더에 인증값을 넣는 것 같다.
		
		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;
		
		try {
			Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
			String token = JWT.create()
					.withIssuer("auth0")
					.sign(algorithm);
		} catch (JWTCreationException e) {
			
		}
	}
	
	public void verifiedToken() {
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;
		try {
		    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer("auth0")
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		} catch (JWTVerificationException exception){
		    //Invalid signature/claims
		}
	}
}
