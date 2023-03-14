package com.test.secondApp.scheduler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;


public class Test {

    public static void main(String[] args) {
        String jobKey = "jobKey임";
        String runJarCommand = "runJarCommand 임";
        String fileFullPath = createFileFullPath("C:\\schedule_test");
        String storeContent = "jobKey: " + jobKey + "\n"
                + "runJarCommand: " + runJarCommand;

        writeTextFile(storeContent, fileFullPath);
    }

    private static void writeTextFile(String storeContent, String fileFullPath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileFullPath);
            fos.write(storeContent.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try { fos.close(); }
            catch (IOException e) { throw new RuntimeException(e); }
        }
    }

    private static String createFileFullPath(String defaultPath) {
        String uuid = UUID.randomUUID().toString();
        return defaultPath + "\\" + uuid + ".txt";
    }

}

