/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.chenjw.issuemonitor.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;



/**
 * 压缩文件相关的工具类
 * 
 * @author junwen.chenjw
 * @version $Id: ZipUtils.java, v 0.1 2014年3月31日 下午4:12:19 junwen.chenjw Exp $
 */
public class ZipUtils {

    /**
     * 创建临时目录
     * 
     * @return 临时目录
     */
    public static File createTempFolder() {
        return new File(new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID()
            .toString());
    }

    /**
     * 解压缩
     * 
     * @param targetFolder 输出目录
     * @param zipFile 压缩文件
     */
    public static void unzip(File targetFolder, File zipFile) {
        ZipInputStream zis = null;
        try {
            ZipEntry entry = null;
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            while ((entry = zis.getNextEntry()) != null) {
                File f = new File(targetFolder.getAbsolutePath() + File.separator + entry.getName());
                FileUtils.forceMkdir(f.getParentFile());
                f.createNewFile();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    IOUtils.copy(zis, fos);
                    fos.flush();
                } finally {
                    IOUtils.closeQuietly(fos);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zis);
        }
    }

    /**
     * 压缩
     * 
     * @param sourceFolder 原始文件目录
     * @param zipFile 压缩文件
     */
    public static void zip(File sourceFolder, File zipFile) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);
            for (File f : sourceFolder.listFiles()) {
                writeZip(f, "", zos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 把文件写入zip流
     * 
     * @param file 文件或目录
     * @param parentPath 父目录
     * @param zos 输出流
     */
    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if (file.exists()) {
            if (file.isDirectory()) {//处理文件夹
                parentPath += file.getName() + File.separator;
                File[] files = file.listFiles();
                for (File f : files) {
                    writeZip(f, parentPath, zos);
                }
            } else {
                FileInputStream fis = null;
                DataInputStream dis = null;
                try {
                    fis = new FileInputStream(file);
                    dis = new DataInputStream(new BufferedInputStream(fis));
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte[] content = new byte[1024];
                    int len;
                    while ((len = fis.read(content)) != -1) {
                        zos.write(content, 0, len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (dis != null) {
                            dis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
