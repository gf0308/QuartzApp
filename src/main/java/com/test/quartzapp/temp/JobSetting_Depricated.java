package com.test.quartzapp.temp;

import com.test.quartzapp.temp.HankerJobA;
import lombok.RequiredArgsConstructor;
import org.quartz.*;

import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JobSetting_Depricated {
    private final Scheduler scheduler;

//    @PostConstruct
    public void start() {
        JobDetail jobDetail = buildJobDetail(HankerJobA.class, new HashMap());
//        JobDetail jobDetail = buildJobDetail(TestJob1.class, new HashMap());

        try {
//            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/30 * * * * ?")); // "0/10 * * * * ?" : 10초 마다 실행 (0초시작해서)
//            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/30 * * * * ?"));
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/5 * * * * ?"));
        } catch(SchedulerException e) {
            e.printStackTrace();
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
