package com.twinkles.asaptrading.service.coin;

import com.twinkles.asaptrading.dto.GenericResponse;
import com.twinkles.asaptrading.dto.request.AddCoinRequest;
import com.twinkles.asaptrading.entity.Coin;
import com.twinkles.asaptrading.exception.AsapTradingException;
import com.twinkles.asaptrading.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinServiceImpl implements CoinService {
    private final CoinRepository coinRepository;

    @Override
    public GenericResponse addCoin(AddCoinRequest addCoinRequest) {
        log.info("Inside addCoin Service");
        if(coinRepository.existsByName(addCoinRequest.getName())){
            throw  new AsapTradingException("Coin with name "+addCoinRequest.getName()+" already exist",400);
        }
        Coin coin = new Coin();
        BeanUtils.copyProperties(addCoinRequest,coin);
        coinRepository.save(coin);
        return GenericResponse.builder().message("Coin with name as " +addCoinRequest.getName()+" successfully updated").successful(true).build();
    }

    @Override
    public GenericResponse updateCoinPrice(AddCoinRequest updateCoinRequest) {
        Coin coin = getCoinByName(updateCoinRequest.getName());
        if(updateCoinRequest.getUnitPrice() != null){
            coin.setUnitPrice(updateCoinRequest.getUnitPrice());
        }
        return GenericResponse.builder().message("Coin price successfully updated").successful(true).build();
    }

    @Override
    public GenericResponse getCoinPrice(String coinName) {
        Coin coin = getCoinByName(coinName);
        return GenericResponse.builder().message("Coin price successfully retrieved").successful(true).data(coin.getUnitPrice()).build();
    }

    @Override
    public GenericResponse deleteCoin(String coinName) {
        Coin coin = getCoinByName(coinName);
        coinRepository.delete(coin);
        return GenericResponse.builder().message("Coin successfully deleted").successful(true).build();
    }

    private Coin getCoinByName(String coinName) {
        return coinRepository.getCoinByName(coinName).orElseThrow(()-> new AsapTradingException("Coin "+ coinName +" does not exist",404));
    }
}
