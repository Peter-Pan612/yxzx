package com.yxzx.exception;

import com.yxzx.model.vo.common.ResultCodeEnum;
import lombok.Data;

/**
 * @author: Peter
 * @description:
 * @date: 2024/1/28 19:35
 */

@Data
public class yxzxException extends RuntimeException{

    private Integer code;

    private String message;

    private ResultCodeEnum resultCodeEnum;

    public yxzxException(ResultCodeEnum resultCodeEnum){
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }




}
