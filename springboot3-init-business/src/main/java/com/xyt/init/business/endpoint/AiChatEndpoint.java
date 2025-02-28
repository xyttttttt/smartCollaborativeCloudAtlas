package com.xyt.init.business.endpoint;

import cn.hutool.json.JSONUtil;
import com.xyt.init.ai.config.GetHttpSessionConfigurator;
import com.xyt.init.business.domain.constant.AiChannelConstant;
import com.xyt.init.business.domain.params.ai.AiRequestParam;
import com.xyt.init.business.domain.service.AiService;
import com.xyt.init.business.factory.AiManagerFactory;
import com.xyt.init.business.manager.TaskManager;
import com.xyt.init.business.utils.SpringContextUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
public class AiChatEndpoint {

    private AiManagerFactory aiManagerFactory;
    private static final TaskManager taskManager = new TaskManager();
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        System.out.println("Connection opened: " + session.getId());
        sessions.add(session);
        aiManagerFactory = SpringContextUtil.getBean(AiManagerFactory.class);
    }

    @OnMessage
    public String onMessage(Session session, String aiRequestParam) {
        System.out.println("Received message: " + aiRequestParam);
        // 如果 aiRequestParam 是带转义字符的字符串，先反序列化
        AiRequestParam requestParam = JSONUtil.toBean(aiRequestParam, AiRequestParam.class);

        try {
            if (requestParam.getIsOver()) {
                // 停止当前会话的任务，并发送确认消息
                taskManager.stopTask(session);
                session.getBasicRemote().sendText("[SYSTEM] 输出已终止，可以继续提问");
                return "";
            }

            String model = requestParam.getModel();
            AiService aiService = resolveAiService(model);

            // 先停止可能存在的旧任务
            taskManager.stopTask(session);

            // 提交新任务
            taskManager.submitTask(session, () ->
                    aiService.chat(requestParam.getText(), message -> {
                        try {
                            if (session.isOpen()) {
                                session.getBasicRemote().sendText(message);
                            }
                        } catch (IOException e) {
                            System.err.println("消息发送失败: " + e.getMessage());
                        }
                    })
            );
        } catch (IOException e) {
            System.err.println("WebSocket操作异常: " + e.getMessage());
        }
        return "";
    }

    private AiService resolveAiService(String model) {
        if (model.equals(AiChannelConstant.ZHIPU.getValue())) {
            return aiManagerFactory.get(AiChannelConstant.ZHIPU);
        } else if (model.equals(AiChannelConstant.DEEPSEEK.getValue())) {
            return aiManagerFactory.get(AiChannelConstant.DEEPSEEK);
        } else if (model.equals(AiChannelConstant.LKE.getValue())) {
            return aiManagerFactory.get(AiChannelConstant.LKE);
        }
        throw new RuntimeException("不支持的AI模型: " + model);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
        sessions.remove(session);
        taskManager.stopTask(session);
    }
}
