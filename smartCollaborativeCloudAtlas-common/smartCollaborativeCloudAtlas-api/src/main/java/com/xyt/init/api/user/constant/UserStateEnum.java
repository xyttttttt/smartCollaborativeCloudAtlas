package com.xyt.init.api.user.constant;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author hollis
 */
@Getter
public enum UserStateEnum {
    /**
     * 创建成功
     */
    INIT("创建成功",1),

    /**
     * 冻结
     */
    FROZEN("已冻结",2),
    /**
     * 注销
     */
    CANCELLED("已注销",3);


    private final String text ;
    private final int value;


    UserStateEnum(String text, int value)
    {
        this.text = text;
        this.value = value;
    }
    public static UserStateEnum getEnumByValue(int value)
    {
        for (UserStateEnum item : UserStateEnum.values())
        {
            if (item.getValue() == value)
            {
                return item;
            }
        }
        return null;
    }
}
