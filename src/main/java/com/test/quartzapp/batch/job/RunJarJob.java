package com.test.quartzapp.batch.job;

import com.test.quartzapp.constant.JobStepConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

/**
 * RunJarJob : JAR 파일을 실행하는 JOB 클래스
 * */

@Slf4j
@Component
public class RunJarJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String jarFileFullPath = (String) jobDataMap.get(JobStepConstants.MAP_KEY_WHEN_STEP_TYPE_RUN_JAR);

        String defaultCommand = "java -jar ";
        String totalCommand = defaultCommand + jarFileFullPath; // ex: "java -jar C:\\schedule_test\\jars\\TestJar4.jar";

        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(totalCommand);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
