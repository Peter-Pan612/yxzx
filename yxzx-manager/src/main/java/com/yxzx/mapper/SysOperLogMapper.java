package com.yxzx.mapper;

import com.yxzx.model.entity.system.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysOperLogMapper {
    void insert(SysOperLog sysOperLog);
}
