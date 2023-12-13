package com.microservice.authservice.service.impl;

import com.microservice.authservice.service.PatientInfoService;
import com.microservice.common.domain.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PatientInfoService patientInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 根据username查询出该用户的信息，封装成UserDetails类型的对象返回
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = patientInfoService.loadUserByUsername(username);
        return new User(userDto.getUsername(), passwordEncoder.encode(userDto.getPassword()),new ArrayList<>());
//        return new User(userDto.getUsername(), userDto.getPassword(),new ArrayList<>());

    }
}
