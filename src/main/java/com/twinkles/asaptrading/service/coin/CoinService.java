package com.twinkles.asaptrading.service.coin;


import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.request.AddCoinRequest;

public interface CoinService {
    GenericResponse addCoin(AddCoinRequest addCoinRequest);
    GenericResponse updateCoinPrice(AddCoinRequest updateCointRequest);
    GenericResponse getCoinPrice(String coinName);
    GenericResponse deleteCoin(String coinName);

}
