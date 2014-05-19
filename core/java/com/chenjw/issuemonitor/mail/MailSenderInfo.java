/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.issuemonitor.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailSenderInfo extends Authenticator {
    // 发送邮件的服务器的IP和端口
    private String              mailServerHost;
    private String              mailServerPort = "25";
    // 邮件发送者的地址
    private String              fromAddress;
    // 邮件接收者的地址
    private String              toAddress;
    // 登录邮件发送服务器的用户名和密码
    private String              userName;
    private String              password;
    // 是否需要身份验证
    private boolean             validate       = false;
    // 邮件主题
    private String              subject;
    // 邮件的文本内容
    private String              content;
    // 邮件附件的文件名
    private Map<String, byte[]> attachs        = new HashMap<String, byte[]>();

    private boolean             ssl            = false;

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        if (ssl) {
            p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            p.put("mail.smtp.socketFactory.fallback", "false");
            p.put("mail.smtp.socketFactory.port", mailServerPort);
        }

        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }

    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Map<String, byte[]> getAttachs() {
        return attachs;
    }

    public void setAttachs(Map<String, byte[]> attachs) {
        this.attachs = attachs;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String textContent) {
        this.content = textContent;
    }
}