package com.qx.infrastructure.adapter.repository;

import com.qx.domain.goods.adapter.IGoodsRepository;
import com.qx.infrastructure.dao.IOrderDao;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class GoodsRepository implements IGoodsRepository {
    @Resource
    private IOrderDao orderDao;

    @Override
    public void changeOrderDealDone(String orderId) {
        orderDao.changeOrderDealDone(orderId);
    }
}
