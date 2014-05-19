package com.chenjw.issuemonitor.core;

import java.io.File;

public interface FileEventHandler {

    public void onStart();

    public void onFileFound(File f, String[] dicNames, String date, String path);

    public void onEnd();
}
