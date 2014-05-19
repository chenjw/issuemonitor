package com.chenjw.issuemonitor.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.model.FindResult;

public class Dics {
    private List<Dic> dics = new ArrayList<Dic>();

    public FindResult find(String str) {
        boolean found = false;
        Set<String> dicNames = new HashSet<String>();
        for (Dic dic : dics) {
            String name = dic.getName();
            Set<String> keywords = dic.getKeywords();
            for (String key : keywords) {
                if (str.contains(key)) {
                    str = StringUtils.replace(str, key, "<font color=red>" + key + "</font>");
                    dicNames.add(name);
                    found = true;
                }
            }
        }
        FindResult result = new FindResult();
        result.setFound(found);
        if (found) {
            result.setReplacedLine(str);
            result.setDicNames(dicNames.toArray(new String[dicNames.size()]));
        }
        return result;
    }

    public List<Dic> getDics() {
        return dics;
    }

    public void setDics(List<Dic> dics) {
        this.dics = dics;
    }

}
