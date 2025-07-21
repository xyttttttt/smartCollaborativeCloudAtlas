package com.xyt.cloudAtlas.business.domain.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.xyt.cloudAtlas.business.utils.AiTokenUtils;
import com.xyt.cloudAtlas.business.domain.service.AiService;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Consumer;

import static java.util.Collections.singletonMap;

@Service
public class LkeAiServiceImpl implements AiService {

    private final String webSocketUrl = "wss://wss.lke.cloud.tencent.com"; // websocket 服务器的 URL
    private final String path = "/v1/qbot/chat/conn/"; // 服务路径

    @Override
    public Object chat(String content, Consumer<String> onMessage) {

        AiTokenUtils aiTokenUtils = new AiTokenUtils();
        String token = aiTokenUtils.getWsToken();


        try {
            IO.Options options = IO.Options.builder()
                    .setQuery("EIO=4")
                    .setPath(path)  // path需要以'/'开头
                    .setTransports(new String[]{WebSocket.NAME})
                    .setAuth(singletonMap("token", token))
                    .setTimeout(2000)
                    .build();
            // 创建 Socket.IO 客户端实例
            Socket socket = IO.socket(webSocketUrl,options);

            // 监听连接成功事件
            socket.on(Socket.EVENT_CONNECT, args1 -> System.out.println("EVENT_CONNECT: " + Arrays.toString(args1)));

            // 监听连接错误事件
            socket.on(Socket.EVENT_CONNECT_ERROR, args2 -> {
                System.out.println("EVENT_CONNECT_ERROR: " + Arrays.toString(args2));
                // 如果连接失败，可能需要更新auth，再发起连接
                options.auth.put("token", aiTokenUtils.getWsToken());
                socket.disconnect().connect();
            });

            // 监听连接断开事件
            socket.on(Socket.EVENT_DISCONNECT, args3 -> System.out.println("EVENT_DISCONNECT: " + Arrays.toString(args3)));

            // 监听reply事件
            socket.on("reply", args4 -> {
                if (args4.length > 0) {
                    JSONObject reply = (JSONObject) args4[0];
                    try {
                        JSONObject payload = reply.getJSONObject("payload");
                        boolean isFromSelf = payload.getBoolean("is_from_self");
                        boolean isFinal = payload.getBoolean("is_final");
                        String respContent = payload.getString("content");
                        if (! isFromSelf) {
                            onMessage.accept(respContent);
                        } else {
                            onMessage.accept("the session end");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Received reply, but args4.length <= 0");
                }
            });

            // 监听error事件
            socket.on("error", args5 -> {
                System.out.println("Received error: " + Arrays.toString(args5));
            });
            socket.connect();
            String reqId = RandomUtil.randomString(18);
            String sid = RandomUtil.randomString(18);
            JSONObject payload = new JSONObject();
            payload.put("request_id",reqId);
            payload.put("content", content);
            payload.put("session_id", sid);
            JSONObject data = new JSONObject();
            data.put("payload",payload);
            // send消息
            socket.emit("send",data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
