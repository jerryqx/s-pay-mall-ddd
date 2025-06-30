package com.qx.infrastructure.gateway;

import com.qx.infrastructure.gateway.dto.LockMarketPayOrderRequestDTO;
import com.qx.infrastructure.gateway.dto.LockMarketPayOrderResponseDTO;
import com.qx.infrastructure.gateway.dto.SettlementMarketPayOrderRequestDTO;
import com.qx.infrastructure.gateway.dto.SettlementMarketPayOrderResponseDTO;
import com.qx.infrastructure.gateway.response.Response;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketService {


    @POST("/api/v1/gbm/trade/lock_market_pay_order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);



    /**
     * 营销结算
     *
     * @param requestDTO 结算商品信息
     * @return 结算结果信息
     */
    @POST("api/v1/gbm/trade/settlement_market_pay_order")
    Call<Response<SettlementMarketPayOrderResponseDTO>> settlementMarketPayOrder(@Body SettlementMarketPayOrderRequestDTO requestDTO);

}
