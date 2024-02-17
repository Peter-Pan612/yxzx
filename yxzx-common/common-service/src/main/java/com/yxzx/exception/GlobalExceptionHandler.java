package com.yxzx.exception;

import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Peter
 * @description:
 * @date: 2024/1/28 19:33
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }


    //自定义异常处理
    @ExceptionHandler(yxzxException.class)
    @ResponseBody
    public Result error(yxzxException e){
        return Result.build(null,e.getResultCodeEnum());
    }





}
