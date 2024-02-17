package com.yxzx.service.impl;

import com.yxzx.commom.log.service.AsyncOperLogService;
import com.yxzx.mapper.SysOperLogMapper;
import com.yxzx.model.entity.system.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 20:20
 */
@Service
public class AsyncOperLogServiceImpl implements AsyncOperLogService {
    @Autowired
    private SysOperLogMapper sysOperLogMapper;

    //保存日志数据
    @Override
    public void saveSysOperLog(SysOperLog sysOperLog) {
        sysOperLogMapper.insert(sysOperLog);

    }
}
