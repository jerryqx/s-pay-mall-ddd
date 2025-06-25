package com.qx.domain.order.adapter.port;

import com.qx.domain.order.model.entity.MarketPayDiscountEntity;
import com.qx.domain.order.model.entity.ProductEntity;

public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);

    MarketPayDiscountEntity lockMarketPayOrder(String userId, String teamId, Long activityId, String productId, String orderId);

}
