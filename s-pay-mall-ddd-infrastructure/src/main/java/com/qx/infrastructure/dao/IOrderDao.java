package com.qx.infrastructure.dao;

 import com.qx.infrastructure.dao.po.PayOrder;
 import org.apache.ibatis.annotations.Mapper;
 import org.apache.ibatis.annotations.Param;

@Mapper
public interface IOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);

}
