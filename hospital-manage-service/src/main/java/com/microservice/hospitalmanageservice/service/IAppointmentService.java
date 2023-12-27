package com.microservice.hospitalmanageservice.service;

import com.microservice.hospitalmanageservice.entity.dto.AppointmentDto;
import com.microservice.hospitalmanageservice.entity.vo.AppointmentVo;

import java.util.List;

public interface IAppointmentService {
    List<AppointmentVo> getByDoctorIdAndDay(String hospitalId, String doctorId, String day);

    void add(String hospitalId, AppointmentDto appointmentDto);

    void removeById(String hospitalId, String id);
    void modify(String hospitalId, AppointmentDto appointmentDto);
}
