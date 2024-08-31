package com.example.webauthn.server.demo;

import com.google.gson.Gson;
import com.google.iot.cbor.CborParseException;
import com.webauthn4j.WebAuthnManager;
import com.webauthn4j.authenticator.Authenticator;
import com.webauthn4j.authenticator.AuthenticatorImpl;
import com.webauthn4j.converter.exception.DataConversionException;
import com.webauthn4j.data.*;
import com.webauthn4j.data.client.Origin;
import com.webauthn4j.data.client.challenge.Challenge;
import com.webauthn4j.server.ServerProperty;
import com.webauthn4j.util.Base64UrlUtil;
import com.webauthn4j.validator.exception.ValidationException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootApplication
@RestController
public class WebauthnServerDemoApplication {
	Logger log = LoggerFactory.getLogger(WebauthnServerDemoApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(WebauthnServerDemoApplication.class, args);
	}

	@CrossOrigin(origins = ApplicationConstants.ORIGIN)
	@PostMapping(value = "/web-authn-registration")
	public ResponseEntity<RegisterResponse>webAuthnRegistration(@RequestBody PublicKeyCredential publicKeyCredential) {
		//		clientExtensionResults from client side
		log.info("Request for web auth registration, body: {}", publicKeyCredential);
		// Client properties
		byte[] attestationObject = Base64UrlUtil.decode(publicKeyCredential.getResponse().getAttestationObject());
		byte[] clientDataJSON =  Base64UrlUtil.decode(publicKeyCredential.getResponse().getClientDataJSON());
		String clientExtensionJSON = new String("{}");
		Set<String> transports = new HashSet<String>();
		Gson gson = new Gson();
		String clientDataJSONString = new String(clientDataJSON, StandardCharsets.UTF_8);
		ClientData clientData = gson.fromJson(clientDataJSONString, ClientData.class);
		log.info("attestationObject: {}", attestationObject);
		log.info("clientDataJSON: {}", clientDataJSON);
		log.info("clientDataJSONString: {}", clientDataJSONString);

		// Server properties
		Origin origin = new Origin(clientData.getOrigin());
		String rpId = "localhost";
		Challenge challenge = new Challenge() {
			@Override
			public @NonNull byte[] getValue() {
				return Base64UrlUtil.decode("NmQ1N2FjYTYtOWJlNy00ZDkxLWIwMDItMDQ1MmIzYWMzMmI1");
			}
		};
		// ?
		byte[] tokenBindingId = ("" +
				"" +
				"").getBytes(StandardCharsets.UTF_8);
		ServerProperty serverProperty = new ServerProperty(origin, rpId, challenge, tokenBindingId);
		log.info("serverProperty: {}", serverProperty);
//		// expectations
		boolean userVerificationRequired = false;
		boolean userPresenceRequired = true;

		RegistrationRequest registrationRequest = new RegistrationRequest(attestationObject, clientDataJSON, clientExtensionJSON, transports);
		log.info("registrationRequest: {}", registrationRequest);
		RegistrationParameters registrationParameters = new RegistrationParameters(serverProperty, userVerificationRequired, userPresenceRequired);
		log.info("registrationParameters: {}", registrationParameters);

		RegistrationData registrationData;
		try {
			registrationData = WebAuthnManager.createNonStrictWebAuthnManager().parse(registrationRequest);
			log.info("registrationData: {}", registrationData);
		} catch (DataConversionException e) {
			// If you would like to handle WebAuthn data structure parse error, please catch DataConversionException
			throw e;
		}

		try {
			WebAuthnManager.createNonStrictWebAuthnManager().validate(registrationData, registrationParameters);
		} catch (ValidationException e) {
			// If you would like to handle WebAuthn data validation error, please catch ValidationException
			throw e;
		}
		// please persist Authenticator object, which will be used in the authentication process.
		Authenticator authenticator =
				new AuthenticatorImpl( // You may create your own Authenticator implementation to save friendly authenticator name
						registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData(),
						registrationData.getAttestationObject().getAttestationStatement(),
						registrationData.getAttestationObject().getAuthenticatorData().getSignCount()
				);
		// save(authenticator); // please persist authenticator in your manner
		byte[] authenticatorByte = SerializationUtils.serialize(authenticator);
		log.info("authenticatorByte: {}", authenticatorByte);
		log.info("getAttestationStatement: {}", authenticator.getAttestationStatement());
		log.info("getAttestedCredentialData: {}", authenticator.getAttestedCredentialData());
		log.info("credentialId: {}", Base64UrlUtil.encodeToString(registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getCredentialId()));
		log.info("Public-key: {}", authenticator.getAttestedCredentialData().getCOSEKey().getPublicKey());

		RegisterResponse response = new RegisterResponse();
		response.setAuthenticatorString(Base64UrlUtil.encodeToString(authenticatorByte));
		response.setCredentialIdString(Base64UrlUtil.encodeToString(registrationData.getAttestationObject().getAuthenticatorData().getAttestedCredentialData().getCredentialId()));

		return ResponseEntity.status(HttpStatus.OK)
				.body(response);
	};

	@CrossOrigin(origins = ApplicationConstants.ORIGIN)
	@PostMapping(value = "/web-authn-authentication")
	public ResponseEntity<String>webAuthnAuthentication(@RequestBody PublicKeyCredential publicKeyCredential) {
		log.info("Request for web auth authentication, body: {}", publicKeyCredential);
		// Client properties
		byte[] credentialId = Base64UrlUtil.decode(publicKeyCredential.getAuthenticator().getCredentialIdString()); // saved credentialId in server side
		byte[] userHandle = Base64UrlUtil.decode(publicKeyCredential.getResponse().getUserHandle());
		byte[] authenticatorData = Base64UrlUtil.decode(publicKeyCredential.getResponse().getAuthenticatorData());
		byte[] clientDataJSON = Base64UrlUtil.decode(publicKeyCredential.getResponse().getClientDataJSON());
		String clientExtensionJSON = "{}";
		byte[] signature = Base64UrlUtil.decode(publicKeyCredential.getResponse().getSignature());
		log.info("credentialId: {}", credentialId);
		log.info("userHandle: {}", userHandle);
		log.info("authenticatorData: {}", authenticatorData);
		log.info("signature: {}", signature);

		Gson gson = new Gson();
		String clientDataJSONString = new String(clientDataJSON, StandardCharsets.UTF_8);
		ClientData clientData = gson.fromJson(clientDataJSONString, ClientData.class);
		log.info("clientDataJSON: {}", clientDataJSON);
		log.info("clientDataJSONString: {}", clientDataJSONString);

		// Server properties
		Origin origin = new Origin(clientData.getOrigin());
		String rpId = "localhost";
		Challenge challenge = new Challenge() {
			@Override
			public @NonNull byte[] getValue() {
				return Base64UrlUtil.decode("NmQ1N2FjYTYtOWJlNy00ZDkxLWIwMDItMDQ1MmIzYWMzMmI1");
			}
		};
		byte[] tokenBindingId = "11111111".getBytes(StandardCharsets.UTF_8);
		ServerProperty serverProperty = new ServerProperty(origin, rpId, challenge, tokenBindingId);

		// expectations
		List<byte[]> allowCredentials = null;
		boolean userVerificationRequired = true;
		boolean userPresenceRequired = true;
		List<String> expectedExtensionIds = Collections.emptyList();

		byte[] authenticatorByte = Base64UrlUtil.decode(publicKeyCredential.getAuthenticator().getAuthenticatorString());
		log.info("authenticatorByte: {}", authenticatorByte);
		Authenticator authenticator = (Authenticator) SerializationUtils.deserialize(authenticatorByte);
		log.info("authenticator:{}", authenticator);
		AuthenticationRequest authenticationRequest =
				new AuthenticationRequest(
						credentialId,
						userHandle,
						authenticatorData,
						clientDataJSON,
						clientExtensionJSON,
						signature
				);
		log.info("authenticationRequest: {}", authenticationRequest);
		AuthenticationParameters authenticationParameters =
				new AuthenticationParameters(
						serverProperty,
						authenticator,
						allowCredentials,
						userVerificationRequired,
						userPresenceRequired
				);
		log.info("authenticationParameters:{}", authenticationParameters);

		AuthenticationData authenticationData;
		try {
			authenticationData = WebAuthnManager.createNonStrictWebAuthnManager().parse(authenticationRequest);
		} catch (DataConversionException e) {
			// If you would like to handle WebAuthn data structure parse error, please catch DataConversionException
			throw e;
		}
		log.info("authenticationData: {}", authenticationData);
		try {
			WebAuthnManager.createNonStrictWebAuthnManager().validate(authenticationData, authenticationParameters);
		} catch (ValidationException e) {
			// If you would like to handle WebAuthn data validation error, please catch ValidationException
			throw e;
		}

		log.info("getSignCount: {}", authenticationData.getAuthenticatorData().getSignCount());
		// please update the counter of the authenticator record
//		updateCounter(
//				authenticationData.getCredentialId(),
//				authenticationData.getAuthenticatorData().getSignCount()
//		);
		return ResponseEntity.status(HttpStatus.OK)
				.body("OK");
	}


	@CrossOrigin(origins = ApplicationConstants.ORIGIN)
	@GetMapping(value = "/get-challenge")
	public ResponseEntity<com.example.webauthn.server.demo.Challenge>getChallenge() {
		com.example.webauthn.server.demo.Challenge challenge = new com.example.webauthn.server.demo.Challenge();
		challenge.setChallenge("NmQ1N2FjYTYtOWJlNy00ZDkxLWIwMDItMDQ1MmIzYWMzMmI1");
		return ResponseEntity.status(HttpStatus.OK)
				.body(challenge);
	}

	@CrossOrigin(origins = ApplicationConstants.ORIGIN)
	@GetMapping(value = "/get-signin-challenge")
	public ResponseEntity<com.example.webauthn.server.demo.Challenge>getSigninChallenge() {
		com.example.webauthn.server.demo.Challenge challenge = new com.example.webauthn.server.demo.Challenge();
		challenge.setChallenge("NmQ1N2FjYTYtOWJlNy00ZDkxLWIwMDItMDQ1MmIzYWMzMmI1");
		return ResponseEntity.status(HttpStatus.OK)
				.body(challenge);
	}

}
