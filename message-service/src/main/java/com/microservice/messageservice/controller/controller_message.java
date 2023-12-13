package com.microservice.messageservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.messageservice.entity.Message;
import com.microservice.messageservice.list_to_json;
import com.microservice.messageservice.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class controller_message {
    @Autowired
    MessageMapper messageMapper;
    /*
    *
    * 此处应调用信息服务，非该接口
    *
    * */
    @GetMapping("/show")
    public String XZW_test(){

        return"{\"information\":123456789}";
    }

    @PostMapping("/get_usermessage")
    public String return_message(@RequestBody String username){
        List<Message> mess=messageMapper.select(username);
        list_to_json list=new list_to_json();
        System.out.println(list.convertListToJson(mess));
        return list.convertListToJson(mess);
    }

    @PostMapping("/send")
    public String send_message(@RequestBody String userbody){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(userbody);

            // 获取 username 和 judge 的值
            String name = jsonNode.get("username").asText();
            int judge = jsonNode.get("judge").asInt();

            // 输出解析结果
            System.out.println("Username: " + name);
            System.out.println("Judge: " + judge);


            return"已收到用户"+name+"的发送信息需求";
        } catch (Exception e) {
            e.printStackTrace();
            return"未收到内容";
        }
    }

}
