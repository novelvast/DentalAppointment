package com.microservice.appointmentservice.service;

import com.microservice.appointmentservice.dto.AppointmentDto;
import com.microservice.appointmentservice.entity.OrderInfo;
import com.microservice.appointmentservice.pojo.BookRequest;
import com.microservice.appointmentservice.pojo.BookResponse;
import com.microservice.appointmentservice.pojo.CancelRequest;
import com.microservice.appointmentservice.service.impl.AppointmentServiceImpl;
import com.microservice.common.api.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> getAppointmentByPatientId(String patientId);
    Integer book(@RequestBody BookRequest bookRequest);

    List<AppointmentDto> getAppointmentByDoctorId(String doctorId);

    Integer patientCancel(CancelRequest cancelRequest);

    Boolean doctorCancel(CancelRequest cancelRequest);

    AppointmentDto getAppointmentById(Integer id);
    void changeApprovalStatus(Integer orderId,String status);
    Integer deleteAppointmentById(Integer id);

    void addTOManage(AppointmentDto appointmentDto);

    void deleteTOManage(AppointmentDto appointmentDto);
}
