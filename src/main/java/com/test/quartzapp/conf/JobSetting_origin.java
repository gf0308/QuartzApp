package com.test.quartzapp.conf;

import com.test.quartzapp.run.HankerJobA;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class JobSetting_origin {

//    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void start(){
        JobDetail jobDetail = buildJobDetail(HankerJobA.class, new HashMap());

        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger("0/2 * * * * ?")); // "0/20 * * * * ?" : 20초 간격으로 실행
        } catch(SchedulerException e){
            e.printStackTrace();
        }
    }

    public Trigger buildJobTrigger(String scheduleExp){
        return TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
    }

    public JobDetail buildJobDetail(Class job, Map params){
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);
        return JobBuilder.newJob(job).usingJobData(jobDataMap).build();
    }
}