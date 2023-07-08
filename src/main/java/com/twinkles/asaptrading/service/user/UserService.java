package com.twinkles.asaptrading.service.user;


import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.UserDto;
import com.twinkles.asaptrading.dto.request.BuyCoinRequest;
import com.twinkles.asaptrading.dto.request.CreateAccountRequest;
import com.twinkles.asaptrading.dto.request.SellCoinRequest;
import com.twinkles.asaptrading.dto.request.UpdateUserDetailsRequest;
import com.twinkles.asaptrading.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUserAccount(String host, CreateAccountRequest createAccountRequest) ;
    UserDto findUserById(String userId);
    List<UserDto> findAllUsers();
    UserDto updateUserProfile(String id, UpdateUserDetailsRequest updateRequest);
    User findUserByEmail(String email);
    void verifyUser(String token);
    GenericResponse buyCoin(BuyCoinRequest buyCoinRequest);
    GenericResponse sellCoin(SellCoinRequest sellCoinRequest);
}
