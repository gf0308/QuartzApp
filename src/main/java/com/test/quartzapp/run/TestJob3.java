package com.test.quartzapp.run;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

/**
 * TestJob3 : TestJar3.jar 를 시작시키는 JOB 클래스
 * */
@Slf4j
@Component
public class TestJob3 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("[" + LocalTime.now() + "] "+ this.getClass().toString());

        String command = "java -jar C:\\schedule_test\\jars\\TestJar3.jar";
        Runtime runtime = Runtime.getRuntime();

        try {
            runtime.exec(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
