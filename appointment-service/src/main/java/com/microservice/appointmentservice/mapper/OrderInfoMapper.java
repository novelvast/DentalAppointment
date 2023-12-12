package com.microservice.appointmentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.appointmentservice.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    @Select("SELECT COUNT(*) FROM order_info WHERE patient_id = #{patientId} AND DATE(order_time) = CURDATE()")
    int getAppointmentCountByPatientToday(@Param("patientId") String patientId);
}
