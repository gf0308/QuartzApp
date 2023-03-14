package com.test.secondApp.scheduler;

import org.quartz.*;
import java.util.Map;

public class JobSetting {

    public static Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                            .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                            .build();

    }

    public static JobDetail buildJobDetail(Class jobClass, Map params, String jobRid) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return JobBuilder.newJob(jobClass)
                        .withIdentity(jobRid)
                        .usingJobData(jobDataMap)
                        .build();
    }

}
