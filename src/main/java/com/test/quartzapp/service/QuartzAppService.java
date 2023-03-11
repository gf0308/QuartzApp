package com.test.quartzapp.service;

import com.test.quartzapp.model.dto.JobMstDto;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.repository.QuartzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzAppService {

    private final QuartzRepository quartzRepository;
    private final Scheduler scheduler;
    private final String scheduleExp = "0/10 * * * * ?";


    public List<JobMstVo> getBatchList() {
        return quartzRepository.selectBatchList();
    }


    public JobKey startJob(Class<?> jobClass, String jobKeyId) { // jobIdClass : TestJob1, TestJob2, ...
        JobDetail jobDetail = buildJobDetail(jobClass, new HashMap(), jobKeyId);

        JobKey jobKey = jobDetail.getKey();
        log.info("start job Key: " + jobKey.toString());

        try {
            scheduler.scheduleJob(jobDetail, buildJobTrigger(scheduleExp));
        } catch(SchedulerException e) {
            log.error(e.getMessage());
        }

        return jobKey;
    }

    public void stopJob(JobKey jobKey) {
        log.info("stop job Key: " + jobKey);

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

    private JobDetail buildJobDetail(Class job, Map params, String jobKeyId) { // withIdentity(JobKey.jobKey("key","group"))
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.putAll(params);

        return JobBuilder.newJob(job)
                .withIdentity(jobKeyId)
                .usingJobData(jobDataMap)
                .build();
    }

}
