package com.qx.trigger.http;


import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.qx.api.IPayService;
import com.qx.api.dto.CreatePayRequestDTO;
import com.qx.api.dto.NotifyRequestDTO;
import com.qx.api.dto.QueryOrderListRequestDTO;
import com.qx.api.dto.QueryOrderListResponseDTO;
import com.qx.api.dto.RefundOrderRequestDTO;
import com.qx.api.dto.RefundOrderResponseDTO;
import com.qx.api.response.Response;
import com.qx.domain.order.model.entity.OrderEntity;
import com.qx.domain.order.model.entity.PayOrderEntity;
import com.qx.domain.order.model.entity.ShopCartEntity;
import com.qx.domain.order.model.valobj.MarketTypeVO;
import com.qx.domain.order.service.IOrderService;
import com.qx.types.common.Constants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/alipay/")
public class AliPayController implements IPayService {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;

    @Resource
    private IOrderService orderService;

    /**
     * http://localhost:8080/api/v1/alipay/create_pay_order
     * <p>
     * {
     * "userId": "10001",
     * "productId": "100001"
     * }
     */
    @RequestMapping(value = "create_pay_order", method = RequestMethod.POST)
    @Override
    public Response<String> createPayOrder(@RequestBody CreatePayRequestDTO createPayRequestDTO) {
        try {
            log.info("商品下单，根据商品ID创建支付单开始 userId:{} productId:{}", createPayRequestDTO.getUserId(), createPayRequestDTO.getUserId());
            String userId = createPayRequestDTO.getUserId();
            String productId = createPayRequestDTO.getProductId();
            String teamId = createPayRequestDTO.getTeamId();
            Integer marketType = createPayRequestDTO.getMarketType();
            Long activityId = createPayRequestDTO.getActivityId();

            // 下单
            PayOrderEntity payOrderEntity = orderService.createOrder(ShopCartEntity.builder()
                    .userId(userId)
                    .productId(productId)
                    .teamId(teamId)
                    .marketTypeVO(MarketTypeVO.valueOf(marketType))
                    .activityId(activityId)
                    .build());

            log.info("商品下单，根据商品ID创建支付单完成 userId:{} productId:{} orderId:{}", userId, productId, payOrderEntity.getOrderId());
            return Response.<String>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("商品下单，根据商品ID创建支付单失败 userId:{} productId:{}", createPayRequestDTO.getUserId(), createPayRequestDTO.getUserId(), e);
            return Response.<String>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * http://xfg-studio.natapp1.cc/api/v1/alipay/alipay_notify_url
     */
    @RequestMapping(value = "alipay_notify_url", method = RequestMethod.POST)
    public String payNotify(HttpServletRequest request) {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }
                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");
                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("支付回调，交易名称: {}", params.get("subject"));
                    log.info("支付回调，交易状态: {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额: {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);

                    // 更新订单未已支付
                    orderService.changeOrderPaySuccess(tradeNo, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(params.get("gmt_payment")));
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }


    @RequestMapping(value = "group_buy_notify", method = RequestMethod.POST)
    @Override
    public String groupBuyNotify(NotifyRequestDTO requestDTO) {
        log.info("拼团回调，组队完成，结算开始 {}", JSON.toJSONString(requestDTO));

        try {
            orderService.changeOrderMarketSettlement(requestDTO.getOutTradeNoList());
            return "success";
        } catch (Exception e) {
            log.info("拼团回调，组队完成，结算失败 {}", JSON.toJSONString(requestDTO));
            return "error";

        }

    }


    @RequestMapping(value = "query_user_order_list", method = RequestMethod.POST)
    @Override
    public Response<QueryOrderListResponseDTO> queryUserOrderList(@RequestBody QueryOrderListRequestDTO requestDTO) {
        try {
            log.info("查询用户订单列表开始 userId:{} lastId:{} pageSize:{}", requestDTO.getUserId(), requestDTO.getLastId(), requestDTO.getPageSize());

            String userId = requestDTO.getUserId();
            Long lastId = requestDTO.getLastId();
            Integer pageSize = requestDTO.getPageSize();

            // 查询订单列表，多查询一条用于判断是否还有更多数据
            List<OrderEntity> orderList = orderService.queryUserOrderList(userId, lastId, pageSize + 1);

            // 判断是否还有更多数据
            boolean hasMore = orderList.size() > pageSize;
            if (hasMore) {
                orderList = orderList.subList(0, pageSize);
            }

            // 转换为响应对象
            List<QueryOrderListResponseDTO.OrderInfo> orderInfoList = orderList.stream().map(order -> {
                QueryOrderListResponseDTO.OrderInfo orderInfo = new QueryOrderListResponseDTO.OrderInfo();
                orderInfo.setId(order.getId());
                orderInfo.setUserId(order.getUserId());
                orderInfo.setProductId(order.getProductId());
                orderInfo.setProductName(order.getProductName());
                orderInfo.setOrderId(order.getOrderId());
                orderInfo.setOrderTime(order.getOrderTime());
                orderInfo.setTotalAmount(order.getTotalAmount());
                orderInfo.setStatus(order.getOrderStatusVO() != null ? order.getOrderStatusVO().getCode() : null);
                orderInfo.setPayUrl(order.getPayUrl());
                orderInfo.setMarketType(order.getMarketType());
                orderInfo.setMarketDeductionAmount(order.getMarketDeductionAmount());
                orderInfo.setPayAmount(order.getPayAmount());
                orderInfo.setPayTime(order.getPayTime());
                return orderInfo;
            }).collect(Collectors.toList());

            QueryOrderListResponseDTO responseDTO = new QueryOrderListResponseDTO();
            responseDTO.setOrderList(orderInfoList);
            responseDTO.setHasMore(hasMore);
            responseDTO.setLastId(!orderList.isEmpty() ? orderList.get(orderList.size() - 1).getId() : null);

            log.info("查询用户订单列表完成 userId:{} 返回订单数量:{} hasMore:{}", userId, orderInfoList.size(), hasMore);
            return Response.<QueryOrderListResponseDTO>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("查询用户订单列表失败 userId:{}", requestDTO.getUserId(), e);
            return Response.<QueryOrderListResponseDTO>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    /**
     * http://localhost:8080/api/v1/alipay/refund_order
     * <p>
     * {
     * "userId": "xfg02",
     * "orderId": "928263928388"
     * }
     */
    @RequestMapping(value = "refund_order", method = RequestMethod.POST)
    @Override
    public Response<RefundOrderResponseDTO> refundOrder(@RequestBody RefundOrderRequestDTO requestDTO) {
        try {
            log.info("用户退单开始 userId:{} orderId:{}", requestDTO.getUserId(), requestDTO.getOrderId());

            String userId = requestDTO.getUserId();
            String orderId = requestDTO.getOrderId();

            // 执行退单操作
            boolean success = orderService.refundOrder(userId, orderId);

            RefundOrderResponseDTO responseDTO = new RefundOrderResponseDTO();
            responseDTO.setSuccess(success);
            responseDTO.setOrderId(orderId);
            responseDTO.setMessage(success ? "退单成功" : "退单失败，订单不存在、已关闭或不属于该用户");

            log.info("用户退单完成 userId:{} orderId:{} success:{}", userId, orderId, success);
            return Response.<RefundOrderResponseDTO>builder()
                    .code(Constants.ResponseCode.SUCCESS.getCode())
                    .info(Constants.ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        } catch (Exception e) {
            log.error("用户退单失败 userId:{} orderId:{}", requestDTO.getUserId(), requestDTO.getOrderId(), e);

            RefundOrderResponseDTO responseDTO = new RefundOrderResponseDTO();
            responseDTO.setSuccess(false);
            responseDTO.setOrderId(requestDTO.getOrderId());
            responseDTO.setMessage("退单失败，系统异常");

            return Response.<RefundOrderResponseDTO>builder()
                    .code(Constants.ResponseCode.UN_ERROR.getCode())
                    .info(Constants.ResponseCode.UN_ERROR.getInfo())
                    .data(responseDTO)
                    .build();
        }
    }
}
