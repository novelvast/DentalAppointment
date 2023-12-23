package com.microservice.appointmentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDto {
    private String username;
    private Integer orderId;
    private String adminUsername;
    private String cancelReason;
    private String auditStatus;
    private String kind;
}
