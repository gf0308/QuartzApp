package com.test.quartzapp.service;

import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobStepVo;
import com.test.quartzapp.repository.QuartzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@SpringBootTest
public class QuartzAppServiceTest {

    private final QuartzRepository quartzRepository;
    private final Scheduler scheduler;

    public List<JobMstVo> getJobList() {
        return quartzRepository.selectJobList();
    }

    public JobMstVo getJobDetail(String jobRid) {
        return quartzRepository.selectJobMaster(jobRid);
    }


    public Date runJob(Class<?> jobClassObj, String jobRid, String scheduleExp) {
        JobDetail jobDetail = buildJobDetail(jobClassObj, new HashMap(), jobRid);
        Date runDate = null;

        try {
            runDate = scheduler.scheduleJob(jobDetail, buildJobTrigger(scheduleExp));
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }

        return runDate;
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

    @Test
    void testGetJobList() {
    }

    @Test
    void getJobMaster() {
        String jobRid = "ec894d56d097488cac23c8d7e5305cac";
        JobMstVo jobMaster = getJobMaster(jobRid);
        System.out.println("jobMaster: " + jobMaster.toString());

    }

    public JobMstVo getJobMaster(String jobRid) {
        JobMstVo jobMstVo = quartzRepository.selectJobMaster(jobRid);
        JobStepVo jobStepVo = quartzRepository.selectJobStep(jobRid);
        jobStepVo.setJobStepParam(quartzRepository.selectJobStepParam(jobStepVo.getRid()));
        jobMstVo.setJobStep(jobStepVo);
        return jobMstVo;
    }
}