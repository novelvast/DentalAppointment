package com.microservice.hospitalmanageservice.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.hospitalmanageservice.service.IHospitalService;
import com.microservice.hospitalmanageservice.entity.po.HospitalPo;
import com.microservice.hospitalmanageservice.mapper.HospitalMapper;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, HospitalPo> implements IHospitalService {
}
