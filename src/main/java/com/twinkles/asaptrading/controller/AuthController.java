package com.twinkles.asaptrading.controller;

import com.twinkles.asaptrading.dto.AsapTradingResponse;
import com.twinkles.asaptrading.dto.UserDto;
import com.twinkles.asaptrading.dto.request.AuthToken;
import com.twinkles.asaptrading.dto.request.CreateAccountRequest;
import com.twinkles.asaptrading.dto.request.LoginRequest;
import com.twinkles.asaptrading.entity.User;
import com.twinkles.asaptrading.exception.AsapTradingException;
import com.twinkles.asaptrading.security.jwt.TokenProvider;
import com.twinkles.asaptrading.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(HttpServletRequest request, @RequestBody @Valid @NotNull CreateAccountRequest accountCreationRequest) throws AsapTradingException {
        String host = request.getRequestURL().toString();
        int index = host.indexOf("/", host.indexOf("/", host.indexOf("/"))+2);
        host = host.substring(0, index+1);
        log.info("Host --> {}", host);
        UserDto userDto = userService.createUserAccount(host, accountCreationRequest);
        log.info("User Dto -> "+userDto);
        AsapTradingResponse apiResponse = AsapTradingResponse.builder()
                .status("success")
                .successful(true)
                .message("user created successfully")
                .data(userDto)
                .build();
        log.info("Returning response > "+apiResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


    @RequestMapping("/verify/{token}")
    public ModelAndView verify(@PathVariable("token") String token) throws AsapTradingException {
        userService.verifyUser(token);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("verification_success");
        return modelAndView;
    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AsapTradingException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        log.info("Authentication --> {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenProvider.generateJWTToken(authentication);
        User user = userService.findUserByEmail(loginRequest.getEmail());
        return new ResponseEntity<>(new AuthToken(token, user.getId()), HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }));
        return errors;
    }
}
