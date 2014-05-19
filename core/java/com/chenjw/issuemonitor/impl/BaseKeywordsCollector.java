package com.chenjw.issuemonitor.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.core.Dic;
import com.chenjw.issuemonitor.core.Dics;
import com.chenjw.issuemonitor.core.KeywordsCollector;

public class BaseKeywordsCollector implements KeywordsCollector {

    private Dic getDic(String dicName) {
        try {
            Dic dic = new Dic();
            dic.setName(dicName);
            Set<String> keywords = new HashSet<String>();
            
            InputStream is=BaseKeywordsCollector.class.getClassLoader().getResourceAsStream(dic.getName() + ".dic");
            keywords.addAll(IOUtils.readLines(is));
            dic.setKeywords(keywords);
            return dic;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Dics collect() {
        Dics dics=new Dics();
        dics.getDics().add(getDic("账单"));
        dics.getDics().add(getDic("公众号"));
        return dics;
    }

}
