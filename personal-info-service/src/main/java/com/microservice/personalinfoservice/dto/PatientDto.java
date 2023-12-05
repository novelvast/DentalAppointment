package com.microservice.personalinfoservice.dto;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Zhao
 * @since 2023-12-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PatientDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String phone;

    private String email;

    private Integer idNumber;

    private String name;

    private String gender;

    private LocalDate birthday;


}
