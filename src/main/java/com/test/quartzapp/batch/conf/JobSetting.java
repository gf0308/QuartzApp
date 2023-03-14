package com.test.quartzapp.batch.conf;

import com.test.quartzapp.constant.JobStepConstants;
import org.quartz.*;
import java.util.Map;

public class JobSetting {

    public static Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    public static JobDetail buildJobDetail(Class<? extends Job> jobClassObject, String jobRid, String stepType, String paramContent) {
        JobDataMap jobDataMap = getJobDataMapByStepType(stepType, paramContent);

        return JobBuilder.newJob(jobClassObject)
                        .withIdentity(jobRid)
                        .usingJobData(jobDataMap)
                        .build();
    }

    private static JobDataMap getJobDataMapByStepType(String stepType, String paramContent) {
        JobDataMap jobDataMap = new JobDataMap();

        switch (stepType) {
            case JobStepConstants.STEP_TYPE_RUN_JAR :
                jobDataMap.put(JobStepConstants.MAP_KEY_WHEN_STEP_TYPE_RUN_JAR, paramContent);
                break;
            case JobStepConstants.STEP_TYPE_CALL_API :
                jobDataMap.put(JobStepConstants.MAP_KEY_WHEN_STEP_TYPE_CALL_API, paramContent);
                break;
            case JobStepConstants.STEP_TYPE_CALL_PROCEDURE :
                jobDataMap.put(JobStepConstants.MAP_KEY_WHEN_STEP_TYPE_CALL_PROCEDURE, paramContent);
                break;
        }

        return jobDataMap;
    }

}
