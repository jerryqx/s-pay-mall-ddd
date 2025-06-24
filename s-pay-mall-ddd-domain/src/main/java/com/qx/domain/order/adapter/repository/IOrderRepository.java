package com.qx.domain.order.adapter.repository;

import com.qx.domain.order.model.aggregate.CreateOrderAggregate;
import com.qx.domain.order.model.entity.OrderEntity;
import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;

public interface IOrderRepository {
    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);
}
