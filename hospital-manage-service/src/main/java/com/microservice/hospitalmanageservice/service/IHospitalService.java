package com.microservice.hospitalmanageservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.microservice.hospitalmanageservice.entity.po.HospitalPo;
import com.microservice.hospitalmanageservice.entity.vo.HospitalVo;

public interface IHospitalService extends IService<HospitalPo> {

    HospitalVo getAdministrator(String hospitalId);
}
