package com.chenjw.issuemonitor.impl;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.core.FileEventHandler;

public class PrintProcessor implements FileEventHandler {

    @Override
    public void onFileFound(File f, String[] dicNames,String date, String path) {
        System.out.println(f.getName() + " " + StringUtils.join(dicNames, ","));
        System.out.println(path);
        System.out.println();
        System.out.println();
        System.out.println();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onEnd() {
    }

}
