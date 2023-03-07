package com.test.quartzapp.run;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalTime;

@Slf4j
//@Component
public class TestJob1 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("["+ LocalTime.now()+"]" + "[log] : " + this.getClass().toString());

        // 'TestRunApp2_HTTPRequestCall' 프로그램 실행호출
        Runtime runtime = Runtime.getRuntime();
        try {
            Process execRsltPrc = runtime.exec("java -jar C:\\schedule_test\\jars\\TestRunApp2_HTTPRequestCall-0.0.1-SNAPSHOT.jar");
            log.info("[실행결과]: " + execRsltPrc.info().toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
