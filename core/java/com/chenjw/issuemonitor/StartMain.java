/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.issuemonitor;

import com.chenjw.issuemonitor.core.Dics;
import com.chenjw.issuemonitor.core.FileEventHandler;
import com.chenjw.issuemonitor.core.KeywordsCollector;
import com.chenjw.issuemonitor.core.Searcher;
import com.chenjw.issuemonitor.impl.BaseKeywordsCollector;
import com.chenjw.issuemonitor.impl.Crash2Searcher;
import com.chenjw.issuemonitor.impl.CrashSearcher;
import com.chenjw.issuemonitor.impl.EmailProcessor;

public class StartMain {
    public static void main(String[] args) {
        KeywordsCollector keywordsCollector = new BaseKeywordsCollector();
        Dics dics = keywordsCollector.collect();
        FileEventHandler processor = new EmailProcessor();
        String path = null;
        if (args != null && args.length >= 1) {
            path = args[0];
        }
        Searcher searcher = new CrashSearcher(path);
        searcher.search(dics, processor);
        System.out.println("finished!");
    }
}
