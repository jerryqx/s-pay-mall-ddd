package com.qx.domain.order.service;

import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;

public interface IOrderService {
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception;

}
