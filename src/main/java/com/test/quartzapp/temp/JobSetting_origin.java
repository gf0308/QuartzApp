package com.test.quartzapp.temp;

import com.test.quartzapp.temp.HankerJobA;
import org.quartz.*;

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