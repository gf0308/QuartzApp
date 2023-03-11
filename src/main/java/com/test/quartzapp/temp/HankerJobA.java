package com.test.quartzapp.temp;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;
import java.time.LocalTime;

@Slf4j
//@Component
public class HankerJobA extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String command = "java -jar C:/schedule_test/jars/TestRunApp1-0.0.1-SNAPSHOT.jar";
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        log.info("[log][" + LocalTime.now() + "]" + "[" + HankerJobA.class + "] 실행 완료");
    }
}
