package com.test.quartzapp.controller;

import com.test.quartzapp.constant.JobConstants;
import com.test.quartzapp.exception.JobException;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobResVo;
import com.test.quartzapp.service.QuartzAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/batch")
@RestController
public class QuartzAppController {

    private final QuartzAppService quartzAppService;

    @PostMapping("/getJobList")
    public List<JobMstVo> getJobList() {
        return quartzAppService.getJobList();
    }

    @PostMapping("/runJob")
    public JobResVo runJob(String jobRid) throws JobException {
        if (isEmpty(jobRid)) throw new JobException(JobConstants.NO_JOB_RID);

        JobMstVo jobDetailVo = quartzAppService.getJobDetail(jobRid);
        if (jobDetailVo == null) throw new JobException(JobConstants.NO_SUCH_JOB);

        String jobClassFullPath = jobDetailVo.getJobClassFullPath(); // "com.test.quartzapp.run.TestJob1"
        if (isEmpty(jobClassFullPath)) throw new JobException(JobConstants.NO_JOB_CLASS_FULL_PATH);

        String scheduleExp = jobDetailVo.getScheduleExp();
        if (isEmpty(scheduleExp)) throw new JobException(JobConstants.NO_SCHEDULE_EXP);

        Class<?> jobClassObj = getClassObjectOfJob(jobClassFullPath);
        if (jobClassObj == null) throw new JobException(JobConstants.NO_JOB_CLASS);

        Date runDate = quartzAppService.runJob(jobClassObj, jobRid, scheduleExp);

        JobResVo jobResVo = new JobResVo();
        jobResVo.setJobRid(jobRid);
        if (runDate != null) {
            jobResVo.setJobProcessResult(JobConstants.RUN_COMPLETE);
            jobResVo.setProcessDateTime(runDate.toString());
        } else {
            jobResVo.setJobProcessResult(JobConstants.RUN_FAIL);
        }

        return jobResVo;
    }


    @PostMapping("/stopJob")
    public JobResVo stopJob(String jobRid) throws JobException {
        if (isEmpty(jobRid)) throw new JobException(JobConstants.NO_JOB_RID);

        boolean stopResult = quartzAppService.stopJob(JobKey.jobKey(jobRid));

        JobResVo jobResVo = new JobResVo();
        jobResVo.setJobRid(jobRid);
        jobResVo.setProcessDateTime(new Date().toString());
        if (stopResult) {
            jobResVo.setJobProcessResult(JobConstants.STOP_COMPLETE);
        } else {
            jobResVo.setJobProcessResult(JobConstants.STOP_FAIL);
        }

        return jobResVo;
    }



    private static Class<?> getClassObjectOfJob(String jobClassFullPath) {
        Class<?> jobIdClass = null;
        try {
            jobIdClass = Class.forName(jobClassFullPath);
        } catch(ClassNotFoundException e) {
            log.error(e.getMessage());
        }
        return jobIdClass;
    }

    private static boolean isEmpty(String jobId) {
        return jobId == null || jobId.length() == 0;
    }

    private static boolean isNotEmpty(String jobId) {
        return !(jobId == null || jobId.length() == 0);
    }


}
