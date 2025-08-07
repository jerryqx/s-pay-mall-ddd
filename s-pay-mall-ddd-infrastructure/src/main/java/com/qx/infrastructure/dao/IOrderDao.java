package com.qx.infrastructure.dao;

 import com.qx.domain.order.model.entity.OrderEntity;
 import com.qx.infrastructure.dao.po.PayOrder;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;

 import java.util.List;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

    void updateOrderPayInfo(PayOrder payOrderReq);

    void changeOrderPaySuccess(PayOrder payOrderReq);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    PayOrder queryOrderByOrderId(String orderId);

    void changeOrderMarketSettlement(@Param("outTradeNoList") List<String> outTradeNoList);

    void changeOrderDealDone(String orderId);

    List<PayOrder> queryUserOrderList(String userId, Long lastId, Integer pageSize);

    PayOrder queryOrderByUserIdAndOrderId(String userId, String orderId);

    boolean refundOrder(String userId, String orderId);
}
