package com.microservice.hospitalmanageservice.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.microservice.hospitalmanageservice.entity.po.HospitalPo;
import com.microservice.hospitalmanageservice.entity.vo.HospitalVo;
import com.microservice.hospitalmanageservice.mapper.HospitalMapper;
import com.microservice.hospitalmanageservice.service.IHospitalService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, HospitalPo> implements IHospitalService {

    @Resource
    private HospitalMapper hospitalMapper;

    @Override
    public HospitalVo getAdministrator(String hospitalId) {
        LambdaQueryWrapper<HospitalPo> wrapper = new LambdaQueryWrapper<HospitalPo>()
                .eq(HospitalPo::getId, hospitalId);
        HospitalPo hospitalPo = hospitalMapper.selectOne(wrapper);
        return BeanUtil.copyProperties(hospitalPo, HospitalVo.class);
    }
}
