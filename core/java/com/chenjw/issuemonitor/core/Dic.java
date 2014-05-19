package com.chenjw.issuemonitor.core;

import java.util.HashSet;
import java.util.Set;

public class Dic {
    private Set<String> keywords = new HashSet<String>();

    private String      name;

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
