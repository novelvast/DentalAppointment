package com.microservice.approvalservice.mapper;

import com.microservice.approvalservice.entity.DoctorCkInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.microservice.approvalservice.entity.PatientCkInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
public interface DoctorCkInfoMapper extends BaseMapper<DoctorCkInfo> {
    @Select("select * from doctor_ck_info")
    public List<DoctorCkInfo> find();

    @Insert("INSERT INTO doctor_ck_info (id, doctor_username,order_id,admin_username,cancel_reason,audit_status) VALUES (#{id}, #{doctorUsername},#{orderId},#{adminUsername},#{cancelReason},#{auditStatus})")
    void save(DoctorCkInfo doctorCkInfo);

    @Update("UPDATE doctor_ck_info SET audit_status=#{auditstatus} WHERE id=#{approvalId}")
    void update(String auditstatus,Integer approvalId);
}
