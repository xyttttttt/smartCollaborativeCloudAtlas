package com.xyt.init.web.util;

import com.xyt.init.base.response.PageResponse;
import com.xyt.init.web.vo.MultiResult;

import static com.xyt.init.base.response.ResponseCode.SUCCESS;

/**
 * @author Hollis
 */
public class MultiResultConvertor {

    public static <T> MultiResult<T> convert(PageResponse<T> pageResponse) {
        MultiResult<T> multiResult = new MultiResult<T>(true, SUCCESS.name(), SUCCESS.name(), pageResponse.getDatas(), pageResponse.getTotal(), pageResponse.getCurrentPage(), pageResponse.getPageSize());
        return multiResult;
    }
}
