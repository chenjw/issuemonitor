package com.chenjw.issuemonitor.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import com.chenjw.issuemonitor.core.Dics;
import com.chenjw.issuemonitor.core.FileEventHandler;
import com.chenjw.issuemonitor.core.Searcher;
import com.chenjw.issuemonitor.model.FindResult;

public class CrashSearcher implements Searcher {

    private File folder = new File("/home/chenjw/test/crash/exceptionFiles");

    public CrashSearcher(String path) {
        if (path != null) {
            folder = new File(path);
        }
    }

    private String getLastSendDate() {
        File file = new File(new File(System.getProperty("java.io.tmpdir")), "crash_analysis");
        if (!file.exists()) {
            return null;
        }
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveLastSendDate(String lastSendDate) {
        File file = new File(new File(System.getProperty("java.io.tmpdir")), "crash_analysis");
        try {
            FileUtils.writeStringToFile(file, lastSendDate);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean greateEqualThan(String d1, String d2) {
        if (StringUtils.isBlank(d2)) {
            return true;
        }
        if (StringUtils.isBlank(d1)) {
            return false;
        }
        return d1.compareTo(d2) >= 0;
    }

    @Override
    public void search(Dics dics, FileEventHandler processor) {
        processor.onStart();
        String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String lastSendDate = getLastSendDate();
        String maxSendDate = null;
        Iterator<File> iter = FileUtils.iterateFiles(folder, new String[] { "txt" }, true);
        while (iter.hasNext()) {
            File f = iter.next();
            String path = StringUtils.substringAfter(f.getAbsolutePath(), folder.getAbsolutePath());
            String foundDate = getDateFromPath(path);
            if (foundDate == null) {
                continue;
            }
            // 如果已经导入了或者大于等于当前天不导入
            if (greateEqualThan(lastSendDate, foundDate) || greateEqualThan(foundDate, now)) {
                continue;
            }
            if (greateEqualThan(foundDate, maxSendDate)) {
                maxSendDate = foundDate;
            }

            LineIterator lineIter = null;
            try {
                lineIter = FileUtils.lineIterator(f, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            boolean found = false;
            while (lineIter.hasNext()) {
                String line = lineIter.nextLine();
                if (line == null) {
                    continue;
                }
                if (line.startsWith("Thread ") && line.endsWith(" Crashed:")) {
                    found = find(f, lineIter, dics, processor);
                    break;
                }
            }

        }
        if (maxSendDate != null) {
            saveLastSendDate(maxSendDate);
        }
        processor.onEnd();
    }

    private String getDateFromPath(String path) {
        String[] ss = StringUtils.split(path, File.separator);
        String date = ss[2];
        if (date == null) {
            return null;
        }
        if (date.length() >= 10) {
            date = StringUtils.left(date, 10);
            return date;
        } else {
            return null;
        }

    }

    private boolean find(File f, LineIterator lineIter, Dics dics, FileEventHandler processor) {
        boolean found = false;
        List<String> lines = new ArrayList<String>();
        Set<String> dicNames = new HashSet<String>();
        while (lineIter.hasNext()) {
            String line = lineIter.nextLine();
            if (StringUtils.isBlank(line)) {
                String path = StringUtils.substringAfter(f.getAbsolutePath(),
                    folder.getAbsolutePath());
                String date = getDateFromPath(path);
                if (date == null) {
                    continue;
                }
                if (found) {

                    processor.onFileFound(f, dicNames.toArray(new String[dicNames.size()]), date,
                        path);
                } else {
                    processor.onFileFound(f, new String[] { "其他" }, date, path);
                }
                return found;
            }
            FindResult findResult = dics.find(line);
            if (findResult.isFound()) {
                line = findResult.getReplacedLine();
                for (String dicName : findResult.getDicNames()) {
                    dicNames.add(dicName);
                }
                found = true;
            }
            lines.add(line);
        }
        return found;

    }

}
