package com.microservice.approvalservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microservice.approvalservice.service.iml.messageuse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class message {
    @Autowired
    messageuse feign;
    public String use(){
//        String USE="{\"id\":1,\"patientUsername\":\"徐焯文\",\"orderId\":1,\"adminUsername\":\"ly\",\"cancelReason\":\"时间不合理\",\"auditStatus\":\"审核通过\"}";
//        try {
//            // 解析主 JSON 字符串
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode mainJsonNode = objectMapper.readTree(USE);
//
//            // 解析嵌套 JSON 字符串
//            JsonNode nestedJsonNode = objectMapper.readTree(feign.XZW_test());
//
//            // 将嵌套 JSON 合并到主 JSON 中
//            if (mainJsonNode instanceof ObjectNode) {
//                ObjectNode mainObjectNode = (ObjectNode) mainJsonNode;
//                mainObjectNode.set("details", nestedJsonNode);
//            }
//
//            // 将合并后的 JSON 转换为字符串
//            String mergedJsonString = objectMapper.writeValueAsString(mainJsonNode);
//
//            // 输出结果
//            System.out.println(mergedJsonString);
//            return mergedJsonString;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return"添加信息失败";
//        }
        return feign.XZW_test();
    }
}
