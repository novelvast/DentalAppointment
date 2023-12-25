package com.microservice.authservice.service.impl;

import com.microservice.authservice.service.PersonalInfoService;
import com.microservice.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonalInfoService personalInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据username查询出该用户的信息，封装成UserDetails类型的对象返回
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String clientId = request.getParameter("client_id");

        UserDto userDto = new UserDto();
        if("client_patient".equals(clientId)){
            userDto = personalInfoService.loadPatientByUsername(username);
        }
        else if("client_doctor".equals(clientId)){
            userDto = personalInfoService.loadDoctorByUsername(username);
        }
        else if("client_admin".equals(clientId)){
            userDto = personalInfoService.loadAdminByUsername(username);
        }
        return new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),new ArrayList<>());
//        return new User(userDto.getUsername(), userDto.getPassword(),new ArrayList<>());

    }
}