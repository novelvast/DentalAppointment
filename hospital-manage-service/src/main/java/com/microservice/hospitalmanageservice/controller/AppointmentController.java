package com.microservice.hospitalmanageservice.controller;

import com.microservice.common.api.CommonResult;
import com.microservice.hospitalmanageservice.entity.dto.AppointmentDto;
import com.microservice.hospitalmanageservice.service.IAppointmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("{hospitalId}/appointment/")
public class AppointmentController {
    @Resource
    private IAppointmentService appointmentService;

    @GetMapping("doctor/{doctorId}/day/{day}")
    public CommonResult getByDoctorAndDay(@PathVariable String hospitalId, @PathVariable String doctorId, @PathVariable String day) {
        try {
            Map<Object, Long> appointmentVos = appointmentService.getByDoctorIdAndDay(hospitalId, doctorId, day);
            return CommonResult.success(appointmentVos);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping
    public CommonResult add(@PathVariable String hospitalId, @RequestBody AppointmentDto appointmentDto) {
        appointmentService.add(hospitalId, appointmentDto);
        return null;
    }

    @DeleteMapping("/{id}")
    public CommonResult remove(@PathVariable String hospitalId, @PathVariable String id) {
        try {
            appointmentService.removeById(hospitalId, id);
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            return CommonResult.failed("删除失败");
        }
    }

    @PutMapping
    public CommonResult modify(@PathVariable String hospitalId, @RequestBody AppointmentDto appointmentDto){
        try {
            appointmentService.modify(hospitalId, appointmentDto);
            return CommonResult.success("修改成功");
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
