package com.qx.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.Subscribe;
import com.qx.domain.goods.service.IGoodsService;
import com.qx.domain.order.adapter.event.PaySuccessMessageEvent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 支付成功回调消息
 * @create 2024-09-30 09:52
 */
@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IGoodsService goodsService;

    @Subscribe
    public void handleEvent(String paySuccessMessageJson) {
        log.info("收到支付成功消息 {}", paySuccessMessageJson);

        PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage = JSON.parseObject(paySuccessMessageJson, PaySuccessMessageEvent.PaySuccessMessage.class);

        log.info("模拟发货（如；发货、充值、开户员、返利），单号:{}", paySuccessMessage.getTradeNo());

        // 变更订单状态 - 发货完成&结算
        goodsService.changeOrderDealDone(paySuccessMessage.getTradeNo());
    }

    @Subscribe
    public void handleEvent(PaySuccessMessageEvent.PaySuccessMessage paySuccessMessage) {
        log.info("收到支付成功消息，订单号: {}", paySuccessMessage.getTradeNo());
    }


}
