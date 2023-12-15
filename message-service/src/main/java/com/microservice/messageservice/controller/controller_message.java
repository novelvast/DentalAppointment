package com.microservice.messageservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.common.api.CommonResult;
import com.microservice.messageservice.entity.Message;
import com.microservice.messageservice.list_to_json;
import com.microservice.messageservice.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class controller_message {
    @Autowired
    MessageMapper messageMapper;

    @PostMapping("/get_usermessage")
    public CommonResult return_message(@RequestParam String username){
        List<Message> mess=messageMapper.select(username);
        return CommonResult.success(mess);
    }

    @PostMapping("/send")
    public String send_message(@RequestBody String userbody){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userbody);

            // 获取 username 和 judge 的值
            String receiver = jsonNode.get("username").asText();
            String sender=jsonNode.get("adminUsername").asText();
            String content=jsonNode.get("auditStatus").asText();

            // 输出解析结果
            System.out.println("Username: " + receiver);
            System.out.println("adminUsername: " + sender);
            System.out.println("content: " + content);
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
            return"已收到用户"+receiver+"的发送信息需求";

            /*
            *
            * 此处需要调用信息服务获取邮箱，还未写，不影响其他功能使用
            *
            * */
        } catch (Exception e) {
            e.printStackTrace();
            return"未收到内容";
        }
    }

}
