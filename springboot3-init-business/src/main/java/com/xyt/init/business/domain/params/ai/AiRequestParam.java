package com.xyt.init.business.domain.params.ai;

import lombok.Data;

@Data
public class AiRequestParam {

    private String text;

    private String model;

    private Boolean isOver;

    private Boolean newChat;
}
