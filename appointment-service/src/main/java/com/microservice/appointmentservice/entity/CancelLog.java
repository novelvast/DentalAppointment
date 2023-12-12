package com.microservice.appointmentservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelLog {
    @TableId(value = "id", type = IdType.AUTO)
    //订单编号
    private Integer id;

    @TableField("user_id")
    private String userId;

    //下单的时间
    @TableField("cancel_time")
    private LocalDateTime cancelTime;

    @TableField("reason")
    private String reason;

    @TableField("extra_info")
    private String extraInfo;

}
