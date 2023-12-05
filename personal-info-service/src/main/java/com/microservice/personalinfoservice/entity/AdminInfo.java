package com.microservice.personalinfoservice.entity;

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
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String phone;

    private String email;

    private String hospital;

    private String name;

    private Integer jobNumber;


}
