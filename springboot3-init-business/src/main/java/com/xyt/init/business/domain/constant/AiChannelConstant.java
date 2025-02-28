package com.xyt.init.business.domain.constant;



public enum AiChannelConstant {


    ZHIPU("zhipu"),

    DEEPSEEK("deepSeek"),

    LKE("lke");

    private String value;

    AiChannelConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


    public static AiChannelConstant getByValue(String value) {
        for (AiChannelConstant aiChannelConstant : AiChannelConstant.values()) {
            if (aiChannelConstant.getValue().equals(value)) {
                return aiChannelConstant;
            }
        }
        return null;
    }
}
