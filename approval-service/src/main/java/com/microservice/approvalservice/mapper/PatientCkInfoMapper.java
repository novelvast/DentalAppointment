package com.microservice.approvalservice.mapper;

import com.microservice.approvalservice.entity.DoctorCkInfo;
import com.microservice.approvalservice.entity.PatientCkInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xu
 * @since 2023-12-12
 */
@Mapper
public interface PatientCkInfoMapper extends BaseMapper<PatientCkInfo> {
    @Select("select * from patient_ck_info where admin_username=#{adminUsername}")
    public List<PatientCkInfo> find_patient(String adminUsername);
    @Select("select * from patient_ck_info where id=#{approvalid}")
    PatientCkInfo find_message_patient(Integer approvalid);
    @Update("UPDATE patient_ck_info SET audit_status=#{auditstatus} WHERE id=#{approvalId}")
    void update(String auditstatus,Integer approvalId);
}
