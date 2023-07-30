package com.twinkles.asaptrading.controller;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.UserDto;
import com.twinkles.asaptrading.dto.request.BuyCoinRequest;
import com.twinkles.asaptrading.dto.request.SellCoinRequest;
import com.twinkles.asaptrading.dto.request.UpdateUserDetailsRequest;
import com.twinkles.asaptrading.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("id") @NotNull @NotBlank String userId){
        UserDto userDto  = userService.findUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(){
        List<UserDto> userDtoList  = userService.findAllUsers();
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/profile/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProfile(@PathVariable("id") @NotNull @NotBlank String userId, @RequestBody UpdateUserDetailsRequest updateUserDetailsRequest){
        UserDto userDto = userService.updateUserProfile(userId, updateUserDetailsRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/buy", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buyCoin(@PathVariable("id") @NotNull @NotBlank String userId, @RequestBody BuyCoinRequest buyCoinRequest){
        buyCoinRequest.setUserId(userId);
        GenericResponse response = userService.buyCoin(buyCoinRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/sell", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sellCoin(@PathVariable("id") @NotNull @NotBlank String userId, @RequestBody SellCoinRequest sellCoinRequest){
        sellCoinRequest.setUserId(userId);
        GenericResponse response = userService.sellCoin(sellCoinRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
