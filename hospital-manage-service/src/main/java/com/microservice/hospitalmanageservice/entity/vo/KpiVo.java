package com.microservice.hospitalmanageservice.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiVo {
    private Integer kpi;
    private LocalDate date;
}
