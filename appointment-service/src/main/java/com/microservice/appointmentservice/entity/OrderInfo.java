package com.microservice.appointmentservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order_info`")
public class OrderInfo {
    @TableId(value = "id", type = IdType.AUTO)
    //订单编号
    private Integer id;

    @TableField("doctor_id")
    private String doctorId;

    @TableField("patient_id")
    private String patientId;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //下单的时间
    @TableField("order_time")
    private LocalDateTime orderTime;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //预约就诊的时间
    @TableField("clinic_time")
    private LocalDateTime clinicTime;

    //预约科室
    @TableField("order_department")
    private String orderDepartment;

    //病情描述
    @TableField("disease_description")
    private String diseaseDescription;

    @TableField("hospital")
    private String hospital;

    @TableField("approval_status")
    private String approvalStatus;
}
