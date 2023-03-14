package com.test.quartzapp.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalTime;

/**
 * CallProcedureJob : Stored Procedure 를 호출하는 JOB 클래스
 * */

@Slf4j
@Component
public class CallProcedureJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
