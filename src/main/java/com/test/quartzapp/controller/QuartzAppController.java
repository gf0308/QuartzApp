package com.test.quartzapp.controller;

import com.test.quartzapp.service.QuartzAppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class QuartzAppController {
    private final QuartzAppService quartzAppService;

    private static final String NO_JOB_ID = "jobId 가 없습니다. jobId를 입력해주세요.";
    private static final String NO_JOB_KEY = "jobKey 가 없습니다. jobKey를 입력해주세요.";
    private static final String START_COMPLETE = "정상적으로 시작됨";
    private static final String STOP_COMPLETE = "정상적으로 중지됨";
    private static final String NO_JOB_CLASS = "해당 Job 클래스가 존재하지 않습니다.";


    @GetMapping("/start")
    public String start(String jobId) {
        if (isEmpty(jobId)) {
            return NO_JOB_ID;
        }

        Class<?> jobClass = getFullPathOfJobClass(jobId); // "com.test.quartzapp.run.TestJob1"
        if (jobClass == null) {
            return NO_JOB_CLASS;
        }

//        JobKey jobKey = quartzAppService.startJob(jobClass);
        JobKey jobKey = quartzAppService.startJob(jobClass);

        return START_COMPLETE + " => jobKey : " + jobKey.toString();
    }

    @GetMapping("/stop")
    public String stop(String jobKey) {
        if (isEmpty(jobKey)) {
            return NO_JOB_KEY;
        }

        // JobKey 타입으로 다시 변환 및 JobKey로 변환시 쓸데없이 또 붙는 맨앞의 Default. 떼어버리기
        new JobKey(jobKey, "");

        quartzAppService.stopJob(JobKey.jobKey(jobKey));

        return STOP_COMPLETE + " => jobKey : " + jobKey;
    }


    private static Class<?> getFullPathOfJobClass(String jobId) {
        Class<?> jobIdClass = null;
        try {
            jobIdClass = Class.forName(jobId);
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
