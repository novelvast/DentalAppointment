package com.microservice.appointmentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.appointmentservice.entity.CancelLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CancelLogMapper extends BaseMapper<CancelLog> {
}
