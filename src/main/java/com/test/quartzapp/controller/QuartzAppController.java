package com.test.quartzapp.controller;

import com.test.quartzapp.model.dto.JobMstDto;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.service.QuartzAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/batch")
@RestController
public class QuartzAppController {
    private final QuartzAppService quartzAppService;

    private static final String NO_JOB_CLASS_FULL_PATH = "jobClassFullPath 가 없습니다. jobClassFullPath 입력해주세요.";
    private static final String NO_JOB_RID = "jobRid 가 없습니다. jobRid를 입력해주세요.";
    private static final String NO_SUCH_JOB = "해당 JOB 이 존재하지 않습니다.";
    private static final String START_COMPLETE = "정상적으로 시작됨";
    private static final String STOP_COMPLETE = "정상적으로 중지됨";
    private static final String NO_JOB_CLASS = "해당 Job 클래스가 존재하지 않습니다.";


    @PostMapping("/getJobList")
    public List<JobMstVo> getJobList() {
        return quartzAppService.getJobList();
    }


    @PostMapping("/runJob")
    public String runJob(String jobRid) {
        if (isEmpty(jobRid)) {
            return NO_JOB_RID;
        }
        // jobRid 를 이용해 job을 조회해서 jobClassFullPath를 얻고 이용하기
        JobMstVo jobDetailVo = quartzAppService.getJobDetail(jobRid);
        if (jobDetailVo == null) {
            return NO_SUCH_JOB;
        }

        String jobClassFullPath = jobDetailVo.getJobClassFullPath();
        if (isEmpty(jobClassFullPath)) {
            return NO_JOB_CLASS_FULL_PATH;
        }

        Class<?> jobClassObj = getClassObjOfJobClass(jobClassFullPath); // "com.test.quartzapp.run.TestJob1"
        if (jobClassObj == null) {
            return NO_JOB_CLASS;
        }

        JobKey jobKey = quartzAppService.runJob(jobClassObj, jobRid);

        return START_COMPLETE + " => jobKey : " + jobKey.toString();
    }

    @PostMapping("/stopJob")
    public void stopJob(String jobRid) throws SchedulerException { // 'stopJob' 호출 시 jobRid 가 전송되어 같이 온다.
        if (isEmpty(jobRid)) {
            throw new SchedulerException(NO_JOB_RID);
        }

        quartzAppService.stopJob(JobKey.jobKey(jobRid));
        // job이 끝나긴 했는데, 아래 리턴문으로 넘어가진 않은것 같음. (아마 위 statement에서 예외로 빠진듯?)
        // 왜냐하면 deleteJob을 호출하면 'SchedulerException'을 던지기 때문 ! => 이후 작업을 뭘 할수가 없음. 여기를 마지막으로 끝내야 한다.
    }


    private static Class<?> getClassObjOfJobClass(String jobClassFullPath) {
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
