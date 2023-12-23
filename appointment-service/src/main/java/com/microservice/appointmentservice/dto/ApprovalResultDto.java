package com.microservice.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResultDto {
    private Integer id;
    private Integer orderId;
    private String adminUsername;
    private String cancelReason;
    private String auditStatus;
    private String username;
    private String kind;
}
