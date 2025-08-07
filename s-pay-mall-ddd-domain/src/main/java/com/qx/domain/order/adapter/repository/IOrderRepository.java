package com.qx.domain.order.adapter.repository;

import com.qx.domain.order.model.aggregate.CreateOrderAggregate;
import com.qx.domain.order.model.entity.OrderEntity;
import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;

import java.util.Date;
import java.util.List;

public interface IOrderRepository {
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    void changeOrderPaySuccess(String orderId, Date payTime);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    OrderEntity queryOrderByOrderId(String orderId);

    void changeMarketOrderPaySuccess(String orderId);

    void changeOrderMarketSettlement(List<String> outTradeNoList);

    List<OrderEntity> queryUserOrderList(String userId, Long lastId, Integer pageSize);

    OrderEntity queryOrderByUserIdAndOrderId(String userId, String orderId);

    boolean refundOrder(String userId, String orderId);
}
