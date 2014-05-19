package com.chenjw.issuemonitor.core;

import java.util.Set;

public interface Searcher {
    
    public void search(Dics dics,FileEventHandler processor);
}
