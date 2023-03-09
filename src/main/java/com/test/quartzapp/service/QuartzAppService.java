package com.test.quartzapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzAppService {
    private final Scheduler scheduler;
    private final String scheduleExp = "0/10 * * * * ?";


    public JobKey startJob(Class<?> jobClass) { // jobIdClass : TestJob1, TestJob2, ...
        JobDetail jobDetail = buildJobDetail(jobClass, new HashMap());
        JobKey jobKey = jobDetail.getKey();
        log.info("start job Key: " + jobKey.toString());

        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger(scheduleExp));
        } catch(SchedulerException e) {
            log.error(e.getMessage());
        }

//        try {
//            Thread.sleep(20000);
//            try {
//                scheduler.deleteJob(jobKey);
//            } catch (SchedulerException se) {
//                log.error(se.getMessage());
//            }
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }

        return jobKey;
    }

    public void stopJob(JobKey jobKey) {
        log.info("delete job Key: " + jobKey);

        try {
            scheduler.deleteJob(jobKey);
        } catch(SchedulerException se) {
            log.error(se.getMessage());
        }
    }


    private Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    private JobDetail buildJobDetail(Class job, Map params) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return JobBuilder.newJob(job).usingJobData(jobDataMap).build();
    }

}
