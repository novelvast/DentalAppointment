package com.microservice.appointmentservice.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Zhao
 * @since 2023-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AdminDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String username;

    private String phone;

    private String email;

    private Integer hospitalId;

    private String name;

    private Integer jobNumber;
}
