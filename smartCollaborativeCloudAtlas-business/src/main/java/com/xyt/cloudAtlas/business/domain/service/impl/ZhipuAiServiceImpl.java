package com.xyt.cloudAtlas.business.domain.service.impl;


import com.xyt.cloudAtlas.business.domain.service.AiService;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class ZhipuAiServiceImpl implements AiService {

    @Resource
    private ClientV4 clientV4;

    @Override
    public Flowable<ModelData> chat(String question, Consumer<String> onMessage) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        ChatMessage sysChatMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "请帮助回答问题");
        ChatMessage userChatMessage = new ChatMessage(ChatMessageRole.USER.value(), question);
        chatMessageList.add(sysChatMessage);
        chatMessageList.add(userChatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .invokeMethod(Constants.invokeMethod)
                .temperature(0.95f)
                .messages(chatMessageList)
                .build();
        Flowable<ModelData> flowable = null;
        try {
            ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
            flowable = invokeModelApiResp.getFlowable();
            flowable
                    .observeOn(Schedulers.io())
                    .map(modelData -> modelData.getChoices().get(0).getDelta().getContent())
                    .doOnNext(message -> {
                        onMessage.accept(message);
                    })
                    .doOnError(throwable -> {
                        onMessage.accept("the session end");
                        throw new RuntimeException(throwable.getMessage());

                    })
                    .doOnComplete(() -> {
                        onMessage.accept("the session end");
                    })
                    .doOnTerminate(() -> {
                        onMessage.accept("the session end");
                    })
                    .blockingSubscribe();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return flowable;
    }
}
