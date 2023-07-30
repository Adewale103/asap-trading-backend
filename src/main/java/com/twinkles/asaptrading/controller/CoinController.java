package com.twinkles.asaptrading.controller;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.request.AddCoinRequest;
import com.twinkles.asaptrading.service.coin.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/coin")
public class CoinController {
    private final CoinService coinService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/admin/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCoin(@RequestBody AddCoinRequest addCoinRequest){
        GenericResponse response = coinService.addCoin(addCoinRequest);
        log.info("Got response from service");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/admin/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCoinPrice(@RequestBody AddCoinRequest addCoinRequest){
        GenericResponse response = coinService.updateCoinPrice(addCoinRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/admin/getCoinPrice/{coinName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCoinPrice(@PathVariable(value = "coinName") String coinName){
        GenericResponse response = coinService.getCoinPrice(coinName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/admin/delete/{coinName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCoin(@PathVariable(value = "coinName") String coinName){
        GenericResponse response = coinService.deleteCoin(coinName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
