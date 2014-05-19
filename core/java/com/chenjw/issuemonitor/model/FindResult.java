package com.chenjw.issuemonitor.model;

import java.util.List;

public class FindResult {
    private boolean  found;

    private String[] dicNames;
    private String   replacedLine;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String[] getDicNames() {
        return dicNames;
    }

    public void setDicNames(String[] dicNames) {
        this.dicNames = dicNames;
    }

    public String getReplacedLine() {
        return replacedLine;
    }

    public void setReplacedLine(String replacedLine) {
        this.replacedLine = replacedLine;
    }

}
