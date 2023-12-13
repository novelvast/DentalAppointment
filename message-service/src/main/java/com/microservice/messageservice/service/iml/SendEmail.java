package com.microservice.messageservice.service.iml;

import com.microservice.messageservice.entity.Email;

public interface SendEmail {

    /**
     * 简单文本邮件
     *
     * @param mailRequest
     * @return
     */
    void sendSimpleMail(Email mailRequest);


    /**
     * Html格式邮件,可带附件
     *
     * @param mailRequest
     * @return
     */
    void sendHtmlMail(Email mailRequest);
}
