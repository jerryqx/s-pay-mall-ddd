package com.qx.domain.order.service;

import com.qx.domain.order.model.entity.MarketPayDiscountEntity;
import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;

import java.util.Date;
import java.util.List;

public interface IOrderService {
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception;

    void changeOrderPaySuccess(String orderId, Date orderTime);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    void changeOrderMarketSettlement(List<String> outTradeNoList);

}
