package com.yxzx.commom.log.aspect;

import com.yxzx.commom.log.annotation.Log;
import com.yxzx.commom.log.service.AsyncOperLogService;
import com.yxzx.commom.log.utils.LogUtil;
import com.yxzx.model.entity.system.SysOperLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 20:07
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private AsyncOperLogService asyncOperLogService;

    //环绕通知
    @Around(value = "@annotation(sysLog)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log sysLog) {
       /* String title = sysLog.title();
        int i = sysLog.businessType();
        System.out.println("title:" + title + ",bussinessType:" + sysLog.businessType());*/

        //业务方法调用之前，封装数据
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(sysLog, joinPoint, sysOperLog);
        //业务方法
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            //System.out.println("在业务方法之后执行");
            //调用业务方法之后，封装数据
            LogUtil.afterHandlLog(sysLog, proceed, sysOperLog, 0, null);
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.afterHandlLog(sysLog, proceed, sysOperLog, 1, e.getMessage());
            throw new RuntimeException();
        }

        //调用service方法，把日志西悉尼添加数据库里面
        asyncOperLogService.saveSysOperLog(sysOperLog);
        return proceed;
    }

}
