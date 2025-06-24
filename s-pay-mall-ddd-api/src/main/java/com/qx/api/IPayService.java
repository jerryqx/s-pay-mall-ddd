package com.qx.api;

import com.qx.api.dto.CreatePayRequestDTO;
import com.qx.api.response.Response;

public interface IPayService
{
    Response<String> createPayOrder(CreatePayRequestDTO createPayRequestDTO);

}
