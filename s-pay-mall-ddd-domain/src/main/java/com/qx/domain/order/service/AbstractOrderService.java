package com.qx.domain.order.service;

import com.qx.domain.order.adapter.port.IProductPort;
import com.qx.domain.order.adapter.repository.IOrderRepository;
import com.qx.domain.order.model.aggregate.CreateOrderAggregate;
import com.qx.domain.order.model.entity.OrderEntity;
import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ProductEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;
import com.qx.domain.order.model.valobj.OrderStatusVO;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractOrderService implements IOrderService {

    protected final IOrderRepository repository;
    protected final IProductPort port;

    public AbstractOrderService(IOrderRepository repository, IProductPort port) {
        this.repository = repository;
        this.port = port;
    }


    @Override
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity) throws Exception {
        // 1. 查询当前用户是否存在掉单和未支付订单
        OrderEntity unpaidOrderEntity = repository.queryUnPayOrder(shopCartEntity);

        if (null != unpaidOrderEntity && OrderStatusVO.PAY_WAIT.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("创建订单-存在，已存在未支付订单。userId:{} productId:{} orderId:{}", shopCartEntity.getUserId(), shopCartEntity.getProductId(), unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .build();
        } else if (null != unpaidOrderEntity && OrderStatusVO.CREATE.equals(unpaidOrderEntity.getOrderStatusVO())) {
            // todo xfg
        }

        ProductEntity productEntity = port.queryProductByProductId(shopCartEntity.getProductId());

        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(productEntity.getProductId(), productEntity.getProductName());

        CreateOrderAggregate orderAggregate = CreateOrderAggregate.builder()
                .userId(shopCartEntity.getUserId())
                .productEntity(productEntity)
                .orderEntity(orderEntity)
                .build();

        this.doSaveOrder(orderAggregate);

        return PayOrderEntity.builder()
                .orderId(orderEntity.getOrderId())
                .payUrl("暂无")
                .build();
    }

    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);

}
