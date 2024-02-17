package com.yxzx.commom.log.service;

import com.yxzx.model.entity.system.SysOperLog;

public interface AsyncOperLogService {

    //保存日志数据
    public abstract void saveSysOperLog(SysOperLog sysOperLog) ;
}
