package com.test.quartzapp.controller;

import com.test.quartzapp.batch.job.CallApiJob;
import com.test.quartzapp.batch.job.CallProcedureJob;
import com.test.quartzapp.batch.job.RunJarJob;
import com.test.quartzapp.constant.JobConstants;
import com.test.quartzapp.constant.JobStepConstants;
import com.test.quartzapp.exception.JobException;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobResVo;
import com.test.quartzapp.model.vo.JobStepParamVo;
import com.test.quartzapp.model.vo.JobStepVo;
import com.test.quartzapp.service.QuartzAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
    private final Scheduler scheduler;

    @PostMapping("/testForJobExecContext")
    public void testForJobExecContext(String stepType) throws SchedulerException {
        quartzAppService.testToAccessJobExecutionContext(stepType);
    }

    @PostMapping("/getJobList")
    public List<JobMstVo> getJobList() {
        return quartzAppService.getJobList();
    }


    @PostMapping("/runJob")
    public JobResVo runJob(String jobRid) throws JobException {
        if (isEmpty(jobRid)) throw new JobException(JobConstants.NO_JOB_RID);

        JobMstVo jobMstVo = quartzAppService.getJobMaster(jobRid);
        if (jobMstVo == null) throw new JobException(JobConstants.NO_SUCH_JOB);

        String scheduleExp = jobMstVo.getScheduleExp();
        if (isEmpty(scheduleExp)) throw new JobException(JobConstants.NO_SCHEDULE_EXP);

        /**
         * Quartz 로 배치를 구현할 때 적합한 JOB depth 는 단일 JOB 이다.
         * 계층적, 다층적 JOB은 적합하지 않다.
         * 즉 실행항목(JOB) 하나만 실행하도록 하는게 적합하며
         * 실행항목(JOB) 실행시 그 소속의 서브실행항목1(STEP),서브실행항목2(STEP), 서브실행항목3(STEP), ... 을 실행하며 step 을 두는 것은
         * 적합하지 않다. (JOB 별 step 들까지 두고 쓰는 것은 '스프링 배치' 에서 잘 지원하고 있다고 함. 쿼츠는 단일 JOB 까지만 적합함)
         * 왜냐하면 Quartz에선 schedule 시키는 최소단위가 Job 클래스 (QuartzJobBean 구현) 인데
         * 이 Job 클래스 마다 한 개의 작업 전용으로만 쓰는 게 적합하지, 그 안에서 일련의 여러가지 작업을 수행시키는 것은 적합하지 않기 때문.
         *
         * => 결론은,
         *    Quartz를 사용하는 상황에서 job_step 을 둔 건 적합하지 않았다.
         *    job_sep, job_step_param 테이블을 다 둘 필요 없이
         *    'job_mst_detail' 같은 테이블 하나만 두고 그 안에 job_type, job_param 등의 칼럼을 두고 사용했으면 됐을 것이다.
         * */

        JobStepVo jobStepVo = jobMstVo.getJobStep();
        if (jobStepVo == null) {
            throw new JobException(JobStepConstants.NO_JOB_STEP);
        }

        String stepType = jobStepVo.getStepType();
        if (stepType == null) {
            throw new JobException(JobStepConstants.NO_JOB_STEP_TYPE);
        }

        Class<? extends Job> jobClassObject = getJobClassObjectByStepType(stepType);
        if (jobClassObject == null) throw new JobException(JobConstants.NO_JOB_CLASS);

        JobStepParamVo jobStepParamVo = jobStepVo.getJobStepParam();
        if (jobStepParamVo == null) throw new JobException(JobStepConstants.NO_JOB_STEP_PARAM);

        String paramContent = jobStepParamVo.getParamContent();
        if (paramContent == null) throw new JobException(JobStepConstants.NO_JOB_STEP_PARAM_CONTENT);

        // JOB 실행
        Date runDate = quartzAppService.runJob(jobClassObject, scheduleExp, jobRid, stepType, paramContent);

        // JOB_STATUS 상태변경
        JobMstVo vo = new JobMstVo();
        vo.setRid(jobRid);
        vo.setJobStatus(JobConstants.JOB_STATUS_ACTIVE);
        quartzAppService.editJobStatus(vo);

        // 응답객체 생성
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

        // JOB_STATUS 상태변경
        JobMstVo vo = new JobMstVo();
        vo.setRid(jobRid);
        vo.setJobStatus(JobConstants.JOB_STATUS_INACTIVE);
        quartzAppService.editJobStatus(vo);

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

    private static boolean isEmpty(String jobId) {
        return jobId == null || jobId.length() == 0;
    }

    private static boolean isNotEmpty(String jobId) {
        return !(jobId == null || jobId.length() == 0);
    }

    private static Class<? extends Job> getJobClassObjectByStepType(String stepType) {
        Class<? extends Job> jobClassObject = null;
        switch (stepType) {
            case JobStepConstants.STEP_TYPE_RUN_JAR :
                jobClassObject = RunJarJob.class;
                break;
            case JobStepConstants.STEP_TYPE_CALL_API :
                jobClassObject = CallApiJob.class;
                break;
            case JobStepConstants.STEP_TYPE_CALL_PROCEDURE :
                jobClassObject = CallProcedureJob.class;
                break;
        }
        return jobClassObject;
    }

}
