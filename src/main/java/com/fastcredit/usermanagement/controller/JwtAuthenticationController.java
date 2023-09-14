package com.fastcredit.usermanagement.controller;



import com.fastcredit.usermanagement.config.JwtTokenUtil;
import com.fastcredit.usermanagement.config.UserConfig;
import com.fastcredit.usermanagement.dto.request.AuthenticateRequest;
import com.fastcredit.usermanagement.dto.response.GenericResponse;
import com.fastcredit.usermanagement.dto.response.JwtResponse;
import com.fastcredit.usermanagement.model.Users;
import com.fastcredit.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.Objects;



@RestController
@RequestMapping("/api/v1")
public class JwtAuthenticationController {

	Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserService userService;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	UserConfig userConfig;

	@Value("${jwt.token.validity}")
	private String jwtTokenValidity;


	/**
	 * @param jwtRequest This takes in json string
	 * @return ResponseEntity object.
	 */
	@RequestMapping(value = "/authenticate-user", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticateRequest jwtRequest) {

		try {

			GenericResponse genericResponse = new GenericResponse();
			Users getUsername = userService.findByUsernameOrEmail(jwtRequest.getUsernameOrEmail());
			if(getUsername == null) {

				genericResponse.setStatus(userConfig.getFail());
				genericResponse.setMessage(userConfig.getInternalServerError());

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
			}

			jwtRequest.setUsernameOrEmail(getUsername.getUsername());

			authenticate(jwtRequest.getUsernameOrEmail(), jwtRequest.getPassword());

			final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(jwtRequest.getUsernameOrEmail());

			final String token = jwtTokenUtil.generateToken(userDetails);

			genericResponse.setStatus(userConfig.getSuccess());
			genericResponse.setMessage(userConfig.getTokenMsg());

			return ResponseEntity.ok(new JwtResponse(genericResponse.getStatus(),genericResponse.getMessage(),token, Integer.valueOf(jwtTokenValidity)));

		} catch(Exception ex) {

			logger.error("error::::"+ex.fillInStackTrace());
			GenericResponse genericResponse = new GenericResponse();
			genericResponse.setStatus(userConfig.getFail());

			if(ex.getMessage().contains("INVALID_CREDENTIALS")) {
				genericResponse.setMessage(userConfig.getInvalidCredential());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(genericResponse);

			}

			genericResponse.setMessage(userConfig.getInternalServerError());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
		}
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}