package com.test.secondApp.scheduler;

import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class JarRunJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String jobKey = jobDetail.getKey().toString();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String runJarCommand = (String)jobDataMap.get("runJarCommand");

        String fileFullPath = createFileFullPath("C:\\schedule_test");
        String storeContent = "jobKey: " + jobKey + "\n"
                + "runJarCommand: " + runJarCommand;

        writeTextFile(storeContent, fileFullPath);
        // 생성되는 텍스트파일 내용에 jobKey와 runJarCommand 내용이 정확히 담겨있다면, Job클래스 실행시 jobExecutionContext 로부터 jobDetail 내용을에 잘 접근해서 이용하고 있다는 것이다.
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
