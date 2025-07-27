package com.xyt.init.base.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DeleteRequest implements Serializable {

    @NotNull(message = "id不能为空")
    private Long id;

    private static final long serialVersionUID = 1L;

}