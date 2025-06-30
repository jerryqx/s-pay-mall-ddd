package com.qx.domain.goods.service;

import com.qx.domain.goods.adapter.IGoodsRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsService implements IGoodsService {

    @Resource
    private IGoodsRepository repository;


    @Override
    public void changeOrderDealDone(String orderId) {
        repository.changeOrderDealDone(orderId);

    }
}
