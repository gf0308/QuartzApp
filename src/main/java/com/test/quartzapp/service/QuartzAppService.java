package com.test.quartzapp.service;

import com.test.quartzapp.model.dto.JobMstDto;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.repository.QuartzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzAppService {

    private final QuartzRepository quartzRepository;
    private final Scheduler scheduler;

    public List<JobMstVo> getJobList() {
        return quartzRepository.selectJobList();
    }

    public JobMstVo getJobDetail(String jobRid) {
        return quartzRepository.selectJobDetail(jobRid);
    }


    public Date runJob(Class<?> jobClassObj, String jobRid, String scheduleExp) {
        JobDetail jobDetail = buildJobDetail(jobClassObj, new HashMap(), jobRid);
        Date runDate = null;

        try {
            runDate = scheduler.scheduleJob(jobDetail, buildJobTrigger(scheduleExp));
        } catch(SchedulerException e) {
            log.error(e.getMessage());
        }

        return runDate;
    }

    public boolean stopJob(JobKey jobKey) {
        boolean stopResult = false;
        try {
            stopResult = scheduler.deleteJob(jobKey);
        } catch(SchedulerException se) {
            log.error(se.getMessage());
        }

        return stopResult;
    }


    private Trigger buildJobTrigger(String scheduleExp) {
        return TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
                .build();
    }

    private JobDetail buildJobDetail(Class job, Map params, String jobRid) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return JobBuilder.newJob(job)
                .withIdentity(jobRid)
                .usingJobData(jobDataMap)
                .build();
    }

}
