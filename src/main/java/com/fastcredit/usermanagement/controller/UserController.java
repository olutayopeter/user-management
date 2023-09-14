package com.fastcredit.usermanagement.controller;

import com.fastcredit.usermanagement.config.UserConfig;
import com.fastcredit.usermanagement.dto.request.UserProfileUpdateRequest;
import com.fastcredit.usermanagement.dto.request.UserRegistrationRequest;
import com.fastcredit.usermanagement.dto.response.GenericResponse;
import com.fastcredit.usermanagement.dto.response.UserProfileResponse;
import com.fastcredit.usermanagement.dto.response.UserRegistrationResponse;
import com.fastcredit.usermanagement.exception.DuplicateEmailException;
import com.fastcredit.usermanagement.exception.DuplicateUsernameException;
import com.fastcredit.usermanagement.model.Users;
import com.fastcredit.usermanagement.repository.UserRepository;
import com.fastcredit.usermanagement.service.UserService;
import com.fastcredit.usermanagement.utils.UserValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    Gson gson;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserConfig userConfig;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    UserValidator userValidator = new UserValidator();


    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {

        UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
        try {

            /**
             * These validate username,password and Email
             */
            if (!userValidator.isValidPassword(request.getPassword())) {

                userRegistrationResponse.setStatus(userConfig.getFail());
                userRegistrationResponse.setMessage(userConfig.getPasswordSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);
            }
            if (!userValidator.isValidUsername(request.getUsername())) {
                userRegistrationResponse.setStatus(userConfig.getFail());
                userRegistrationResponse.setMessage(userConfig.getUsernameSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);
            }
            if (!userValidator.isValidEmail(request.getEmail())) {
                userRegistrationResponse.setStatus(userConfig.getFail());
                userRegistrationResponse.setMessage(userConfig.getEmailSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);
            }

            /**
             * check for duplicate username
             */
            // Check for duplicate username
            if (userService.findByUsername(request.getUsername()).isPresent()) {
                DuplicateUsernameException duplicateUsernameException = new DuplicateUsernameException(userConfig.getDuplicateUsername());
                userRegistrationResponse.setStatus(userConfig.getFail());
                userRegistrationResponse.setMessage(userConfig.getDuplicateUsername());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);

            }

            /**
             * check for duplicate email
             */
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                DuplicateEmailException duplicateEmailException = new DuplicateEmailException(userConfig.getDuplicateEmail());
                userRegistrationResponse.setStatus(userConfig.getFail());
                userRegistrationResponse.setMessage(userConfig.getDuplicateEmail());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userRegistrationResponse);
            }

            /**
             * Hash the password
             */
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            /**
             * create a new user
             */
            Users user = new Users();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(encodedPassword);

            /**
             * save the user to the database
             */
            userRepository.save(user);

            userRegistrationResponse.setStatus(userConfig.getSuccess());
            userRegistrationResponse.setMessage(userConfig.getRegistrationMsg());

            return ResponseEntity.status(HttpStatus.OK).body(userRegistrationResponse);

        } catch (DuplicateUsernameException ex) {
            logger.error("registration error:" + ex.fillInStackTrace());
            userRegistrationResponse.setStatus(userConfig.getFail());
            userRegistrationResponse.setMessage(userConfig.getDuplicateUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRegistrationResponse);

        } catch (DuplicateEmailException ex) {
            logger.error("registration error:" + ex.fillInStackTrace());
            userRegistrationResponse.setStatus(userConfig.getFail());
            userRegistrationResponse.setMessage(userConfig.getDuplicateEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userRegistrationResponse);
        } catch (Exception ex) {

            logger.error("registration error:" + ex.fillInStackTrace());
            userRegistrationResponse.setStatus(userConfig.getFail());
            userRegistrationResponse.setMessage(userConfig.getUnExpectedError());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userRegistrationResponse);

        }
    }


    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(Authentication authentication) {

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        try {

            /**
             * This gets the username of the authenticated user
             */
            String username = authentication.getName();

            /**
             *  Retrieve user profile based on the authenticated username
             */
            Optional<Users> user = userRepository.findByUsername(username);
            if (!user.isPresent()) {

                userProfileResponse.setStatus(userConfig.getFail());
                userProfileResponse.setMessage(userConfig.getNotFound());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(userProfileResponse);

            }

            userProfileResponse.setStatus(userConfig.getSuccess());
            userProfileResponse.setMessage(userConfig.getRetrieveProfileMsg());
            userProfileResponse.setUsername(user.get().getUsername());
            userProfileResponse.setEmail(user.get().getEmail());

            return ResponseEntity.status(HttpStatus.OK).body(gson.fromJson(objectMapper.writeValueAsString(userProfileResponse), UserProfileResponse.class));


        } catch (Exception ex) {
            logger.error("Get user profile error:" + ex.fillInStackTrace());
            userProfileResponse.setStatus(userConfig.getFail());
            userProfileResponse.setMessage(userConfig.getUnExpectedError());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userProfileResponse);
        }

    }



    @PutMapping("/update-profile")
    public ResponseEntity<GenericResponse> updateUserProfile(Authentication authentication, @Valid @RequestBody UserProfileUpdateRequest request) {

        GenericResponse genericResponse = new GenericResponse();
        try {

            /**
             *  These validate username,password and email
             */
            if (!userValidator.isValidPassword(request.getPassword())) {

                genericResponse.setStatus(userConfig.getFail());
                genericResponse.setMessage(userConfig.getPasswordSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(genericResponse);
            }
            if (!userValidator.isValidUsername(request.getUsername())) {
                genericResponse.setStatus(userConfig.getFail());
                genericResponse.setMessage(userConfig.getUsernameSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(genericResponse);
            }
            if (!userValidator.isValidEmail(request.getEmail())) {
                genericResponse.setStatus(userConfig.getFail());
                genericResponse.setMessage(userConfig.getEmailSize());

                return ResponseEntity.status(HttpStatus.ACCEPTED).body(genericResponse);
            }

            /**
             *  Check if the 'password' field is not empty in the request
             */

            if (StringUtils.isBlank(request.getPassword())) {

                genericResponse.setStatus(userConfig.getFail());
                genericResponse.setMessage(userConfig.getEmptyPassword());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);

            }
            /**
             *  Validate and update the user's profile based on the authenticated user
             */
            Optional<Users> user = userRepository.findByUsername(authentication.getName());
            if(!user.isPresent()){

                genericResponse.setStatus(userConfig.getFail());
                genericResponse.setMessage(userConfig.getNotFound());
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(genericResponse);
            }

            /**
             *  Update user profile fields as needed (e.g., email)
             */
            Users updateUser = userService.findByUsernameOrEmail(user.get().getUsername());
            /**
             *  Hash the password
             */
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            updateUser.setEmail(request.getEmail());
            updateUser.setUsername(request.getUsername());
            updateUser.setPassword(encodedPassword);
            userRepository.save(updateUser);

            genericResponse.setStatus(userConfig.getSuccess());
            genericResponse.setMessage(userConfig.getProfileUpdateMsg());
            return ResponseEntity.status(HttpStatus.OK).body(genericResponse);


        } catch (ValidationException ex) {
            logger.error("user profile update error: " + ex.fillInStackTrace());
            genericResponse.setStatus(userConfig.getFail());
            genericResponse.setMessage(userConfig.getValidationError());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(genericResponse);
        } catch (Exception ex) {

            logger.error("user profile update error: " + ex.fillInStackTrace());
            genericResponse.setStatus(userConfig.getFail());
            genericResponse.setMessage(userConfig.getUnExpectedError());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genericResponse);
        }

    }
}
