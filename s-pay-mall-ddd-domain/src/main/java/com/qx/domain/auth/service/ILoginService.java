package com.qx.domain.auth.service;

import java.io.IOException;

public interface ILoginService {

    String createQrCodeTicket() throws Exception;

    String createQrCodeTicket(String sceneStr) throws IOException;


    String checkLogin(String ticket);

    String checkLogin(String ticket, String sceneStr);



    void saveLoginState(String ticket, String openid) throws IOException;

}
