package com.microservice.appointmentservice.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    private String patientId;

    private String doctorId;

    private LocalDateTime orderTime;

    private LocalDateTime clinicTime;

    private String orderDepartment;

    private String diseaseDescription;

    private String hospital;
}
