package com.microservice.messageservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.common.api.CommonResult;
import com.microservice.messageservice.entity.Email;
import com.microservice.messageservice.entity.Message;
import com.microservice.messageservice.mapper.MessageMapper;
import com.microservice.messageservice.service.impl.Feign_Service;
import com.microservice.messageservice.service.impl.Message_service;
import com.microservice.messageservice.service.impl.SendEmail;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class messageservice implements Message_service {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    Feignservice feignservice;
    @Autowired
    SendEmail sendEmail;
    @Override
    public CommonResult return_message(String username){
        List<Message> mess=messageMapper.select(username);
        return CommonResult.success(mess);
    }
    @GlobalTransactional
    @Override
    public String send_message(String userbody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userbody);

            // 获取 username 和 judge 的值
            String receiver = jsonNode.get("username").asText();
            String sender=jsonNode.get("adminUsername").asText();
            String content=jsonNode.get("auditStatus").asText();
            String kind=jsonNode.get("kind").asText();

            // 输出解析结果
            System.out.println("Username: " + receiver);
            System.out.println("adminUsername: " + sender);
            System.out.println("content: " + content);
            System.out.println("kind: " + kind);

            Message message=new Message();
            message.setReceiver(receiver);
            message.setSender(sender);
            message.setContent(content);
            LocalDateTime currentTime = LocalDateTime.now();
            // 定义日期时间格式（可选）
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // 格式化当前时间
            String formattedTime = currentTime.format(formatter);
            message.setTime(formattedTime);
            messageMapper.insert(message);

//            String user_email = "2153275@tongji.edu.cn";
//            if(kind.equals("医生")){
//                user_email=feignservice.get_doctor(receiver);
//            }
//            else if(kind.equals("患者")) {
//                user_email=feignservice.get_patient(receiver);
//            }
//            Email email=new Email();
//            email.setSendTo(user_email);
//            email.setSubject("预约结果");
//            email.setText(content);
//            sendEmail.sendSimpleMail(email);
            /*else
             *
             * 此处需要调用信息服务获取邮箱，还未写，不影响其他功能使用
             *
             * */

            return"已收到用户"+receiver+"的发送信息需求并成功发送邮件";
        } catch (Exception e) {
            e.printStackTrace();
            return"未收到内容";
        }
    }
}
