package com.chenjw.issuemonitor.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.core.Dic;
import com.chenjw.issuemonitor.core.Dics;
import com.chenjw.issuemonitor.core.KeywordsCollector;

public class FileKeywordsCollector implements KeywordsCollector {
    public Dics collect() {
        Dics dics = new Dics();
        collectProject(dics, "/home/chenjw/test/crash/BillApp", "账单");
        collectProject(dics, "/home/chenjw/test/crash/PublicPlatform", "公众号");
        //save(dics);
        return dics;
    }

    private void save(Dics dics) {

        for (Dic dic : dics.getDics()) {
            try {
                File writerFile = new File(new File("resources"), dic.getName() + ".dic");
                Writer writer = new FileWriter(writerFile);
                for (String key : dic.getKeywords()) {

                    writer.write(key + "\n");

                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void collectProject(Dics dics, String path, String name) {
        File folder = new File(path);
        Set<String> result = new HashSet<String>();
        Iterator<File> iter = FileUtils.iterateFiles(folder, new String[] { "h" }, true);
        while (iter.hasNext()) {
            File f = iter.next();
            result.add(StringUtils.substringBeforeLast(f.getName(), "."));
        }
        Dic dic = new Dic();
        dic.setKeywords(result);
        dic.setName(name);
        dics.getDics().add(dic);
    }
}
