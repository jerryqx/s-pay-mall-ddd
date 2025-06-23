package com.qx.domain.order.adapter.port;

import com.qx.domain.order.model.entity.ProductEntity;

public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);

}
