package com.test.quartzapp.controller;

import com.test.quartzapp.model.dto.JobMstDto;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.service.QuartzAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
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
    private static final String NO_JOB_KEY_ID = "jobKeyId 가 없습니다. jobKeyId를 입력해주세요.";
    private static final String START_COMPLETE = "정상적으로 시작됨";
    private static final String STOP_COMPLETE = "정상적으로 중지됨";
    private static final String NO_JOB_CLASS = "해당 Job 클래스가 존재하지 않습니다.";


    @PostMapping("/getJobList")
    public List<JobMstVo> getJobList() {
        return quartzAppService.getJobList();
    }


    @GetMapping("/runBatch")
    public String startJob(String jobClassFullPath, String jobKeyId) {
        if (isEmpty(jobClassFullPath)) {
            return NO_JOB_CLASS_FULL_PATH;
        }
        if (isEmpty(jobKeyId)) {
            return NO_JOB_KEY_ID;
        }

        Class<?> jobClass = getFullPathOfJobClass(jobClassFullPath); // "com.test.quartzapp.run.TestJob1"
        if (jobClass == null) {
            return NO_JOB_CLASS;
        }

        JobKey jobKey = quartzAppService.startJob(jobClass, jobKeyId);

        return START_COMPLETE + " => jobKey : " + jobKey.toString();
    }

    @GetMapping("/stop")
    public String stopJob(String jobKeyId) {
        if (isEmpty(jobKeyId)) {
            return NO_JOB_KEY_ID;
        }

        // JobKey 타입으로 다시 변환 및 JobKey로 변환시 쓸데없이 또 붙는 맨앞의 Default. 떼어버리기
//        new JobKey(jobKey, "");
//        jobKey = jobKey.replaceAll("DEFAULT.", "");
//        JobKey jobKey1 = JobKey.jobKey(jobKey);
//        quartzAppService.stopJob(jobKey1);

        quartzAppService.stopJob(JobKey.jobKey(jobKeyId));

//        + " => jobKey : " + jobKey;
        return STOP_COMPLETE;
    }


    private static Class<?> getFullPathOfJobClass(String jobClassFullPath) {
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
