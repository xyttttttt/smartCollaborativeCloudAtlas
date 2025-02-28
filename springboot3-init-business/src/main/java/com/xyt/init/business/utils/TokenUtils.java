package com.xyt.init.business.utils;

import cn.hutool.json.JSONObject;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.beans.factory.annotation.Value;


public class TokenUtils {

    private final int connTypeApi = 5;
    private static String visitorBizId = "202403180001"; //  访客 ID（外部系统提供，需确认不同的访客使用不同的 ID）

    @Value("${ai.lke.appKey}")// API 访客
    private static String botAppkey;  // 机器人密钥，不是BotBizId (从运营接口人处获取)

    @Value("${ai.lke.secretId}")
    private String secretId;// 填入腾讯云AKSK密钥(从腾讯云控制台获取)

    @Value("${ai.lke.secretKey}")
    private String secretKey;// 填入腾讯云AKSK密钥(从腾讯云控制台获取)

    public String getWsToken() {
        try {
            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setRootDomain("tencentcloudapi.com");
            httpProfile.setReqMethod(HttpProfile.REQ_POST);
            httpProfile.setProtocol(HttpProfile.REQ_HTTPS);

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("lke", "2023-11-30", cred, "ap-guangzhou", clientProfile);
            JSONObject params = new JSONObject();
            params.put("Type", connTypeApi);
            params.put("BotAppKey", botAppkey);
            params.put("VisitorBizId", visitorBizId);
            String respJsonStr = client.call("GetWsToken", params.toString());

            JSONObject resp = new JSONObject(respJsonStr);
            JSONObject respData = resp.getJSONObject("Response");
            return respData.getStr("Token");
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

}
