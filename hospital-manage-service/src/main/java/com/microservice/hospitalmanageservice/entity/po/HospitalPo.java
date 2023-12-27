package com.microservice.hospitalmanageservice.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "hospital_message")
public class HospitalPo {
    private int id;
    private String hospitalName;
    @TableField(value = "EWNS")
    private String EWNS;
    private String position;
    private String introduction;
    private String photo;
    private String administrator;
}
