package com.chenjw.issuemonitor.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.core.FileEventHandler;
import com.chenjw.issuemonitor.mail.MailSenderInfo;
import com.chenjw.issuemonitor.mail.SimpleMailSender;
import com.chenjw.issuemonitor.utils.ZipUtils;

public class EmailProcessor implements FileEventHandler {
    private Map<String, File> dicFolders = new HashMap<String, File>();

    @Override
    public void onStart() {
    }

    private String getKey(String dicName, String date) {
        // IPHONE_1ND\8.1.0\2014-05-05\8.1.0.041402

        return dicName + "_" + date;
    }

    private String getDicNameFromKey(String key) {
        return StringUtils.substringBefore(key, "_");
    }

    private String getDateFromKey(String key) {
        return StringUtils.substringAfter(key, "_");
    }

    private int countFileNum(File folder) {
        int num = 0;
        Iterator<File> iterator = FileUtils.iterateFiles(folder, null, true);
        while (iterator.hasNext()) {
            iterator.next();
            num++;
        }
        return num;
    }

    @Override
    public void onEnd() {
        for (Entry<String, File> entry : dicFolders.entrySet()) {
            try {
                String key = entry.getKey();
                String dicName = getDicNameFromKey(key);
                String date = getDateFromKey(key);
                File sourceFolder = entry.getValue();
                File zipFile;
                zipFile = File.createTempFile("temp", ".zip");

                int nums = countFileNum(sourceFolder);
                ZipUtils.zip(sourceFolder, zipFile);
                sendMail(zipFile, dicName, date, nums);
                // 清理临时文件
                FileUtils.deleteQuietly(zipFile);
                FileUtils.deleteDirectory(sourceFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void save(File f, String dicName, String date, String path) {
        String key = this.getKey(dicName, date);
        File folder = dicFolders.get(key);
        if (folder == null) {
            folder = ZipUtils.createTempFolder();
            dicFolders.put(key, folder);
        }
        try {
            File writeFile = new File(folder, path);
            FileUtils.copyFile(f, writeFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMail(File zipFile, String dicName, String date, int errorNum) {
        try {

            MailSenderInfo mailInfo = new MailSenderInfo();
            mailInfo.setSsl(false);
            mailInfo.setMailServerHost("smtp.wo.com.cn");
            mailInfo.setValidate(true);
            mailInfo.setUserName("18698558825@wo.com.cn");
            mailInfo.setPassword("qw111111");// 你的邮箱密码
            mailInfo.setFromAddress("18698558825@wo.com.cn");
            mailInfo.setToAddress("junwen.chenjw@alibaba-inc.com");
            String subject = date + " 客户端crash报警 <" + dicName + ">";
            mailInfo.setSubject(subject);
            mailInfo.setContent("一共有 " + errorNum + " 个错");

            mailInfo.getAttachs().put(dicName + "-" + date + ".zip",
                FileUtils.readFileToByteArray(zipFile));
            // 这个类主要来发送邮件
            SimpleMailSender.sendHtmlMail(mailInfo);// 发送文体格式
            System.out.println("send " + subject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileFound(File f, String[] dicNames, String date, String path) {
        for (String dicName : dicNames) {
            save(f, dicName, date, path);
        }
    }

}
