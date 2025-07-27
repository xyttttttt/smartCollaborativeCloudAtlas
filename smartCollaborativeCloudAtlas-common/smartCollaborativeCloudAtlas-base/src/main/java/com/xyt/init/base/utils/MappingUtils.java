package com.xyt.init.base.utils;

import cn.hutool.json.JSONUtil;

import java.util.List;

public class MappingUtils {
    public static String listToJson(List<String> list) {
        return JSONUtil.toJsonStr(list);
    }
}