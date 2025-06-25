package com.qx.infrastructure.gateway;

import com.qx.infrastructure.gateway.dto.LockMarketPayOrderRequestDTO;
import com.qx.infrastructure.gateway.dto.LockMarketPayOrderResponseDTO;
import com.qx.infrastructure.gateway.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketService {


    @POST("/api/v1/gbm/trade/lock_market_pay_order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);
}
