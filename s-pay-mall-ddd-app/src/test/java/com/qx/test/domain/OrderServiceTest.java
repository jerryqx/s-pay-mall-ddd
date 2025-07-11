package com.qx.test.domain;

 import com.alibaba.fastjson.JSON;
 import com.qx.domain.order.model.entity.PayOrderEntity;
 import com.qx.domain.order.model.entity.ShopCartEntity;
 import com.qx.domain.order.model.valobj.MarketTypeVO;
 import com.qx.domain.order.service.IOrderService;
 import jakarta.annotation.Resource;
 import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Resource
    private IOrderService orderService;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartEntity shopCartEntity = new ShopCartEntity();
        shopCartEntity.setUserId("qx01");
        shopCartEntity.setProductId("9890001");
        //shopCartEntity.setTeamId("12234396");
        shopCartEntity.setActivityId(100123L);
        shopCartEntity.setMarketTypeVO(MarketTypeVO.GROUP_BUY_MARKET);

        PayOrderEntity payOrderEntity = orderService.createOrder(shopCartEntity);

        log.info("请求参数:{}", JSON.toJSONString(shopCartEntity));
        log.info("测试结果:{}", JSON.toJSONString(payOrderEntity));
    }

}
