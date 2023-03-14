package com.test.quartzapp.service;

import com.test.quartzapp.batch.conf.JobSetting;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobStepVo;
import com.test.quartzapp.repository.QuartzRepository;
import com.test.quartzapp.temp.JarRunJob;
import com.test.quartzapp.temp.JobSetting_deprecated;
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

    public JobMstVo getJobMaster(String jobRid) {
        JobMstVo jobMstVo = quartzRepository.selectJobMaster(jobRid);
        JobStepVo jobStepVo = quartzRepository.selectJobStep(jobRid);
        jobStepVo.setJobStepParam(quartzRepository.selectJobStepParam(jobStepVo.getRid()));
        jobMstVo.setJobStep(jobStepVo);
        return jobMstVo;
    }

    public Date runJob(Class<? extends Job> jobClassObject, String scheduleExp, String jobRid, String stepType, String paramContent) {
        Date runDate = null;
        JobDetail jobDetail = JobSetting.buildJobDetail(jobClassObject, jobRid, stepType, paramContent);
        Trigger trigger = JobSetting.buildJobTrigger(scheduleExp);

        try {
            runDate = scheduler.scheduleJob(jobDetail, trigger);
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

    public void editJobStatus(JobMstVo vo) {
        quartzRepository.updateJobStatus(vo);
    }

    public void testToAccessJobExecutionContext(String stepType) throws SchedulerException {
        if (stepType.equals("RUN JAR")) {
            // stepType : "RUN JAR" 인 케이스 => 사용할 JOB 클래스: JarRunJob.class
            Class JobClassObject = JarRunJob.class;

            String paramContent = "C:\\schedule_test\\jars\\TestJar1.jar"; // 어디로부턴가 받아온 파라미터값, : "C:\schedule_test\jars\TestJar1.jar"
            String scheduleExp = "0/5 * * * * ?";                          // 어딘가로부터 받아온 스케줄표현식 값 : "0/5 * * * * ?"
            String jobRid = "ec894d56d097488cac23c8d7e5305cac";                 // 어딘가로부터 받아온 jobRid: "ec894d56d097488cac23c8d7e5305cac"

            // jobDataMap 재료 작성
            String runJarCommand = "java -jar " + paramContent;
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("runJarCommand", runJarCommand);

            // jobDetail 빌드
            JobDetail jobDetail = JobSetting_deprecated.buildJobDetail(JobClassObject, hashMap, jobRid);
            // JobTrigger 생성
            Trigger trigger = JobSetting_deprecated.buildJobTrigger(scheduleExp);
            // 스케줄러에서 스케줄 실행
            Date date = scheduler.scheduleJob(jobDetail, trigger);

            System.out.println("스케줄 정상 실행 / 실행시각: " + date);

        } else if(stepType.equals("CALL API")) {
            // ...
        } else if(stepType.equals("CALL PROCEDURE")) {
            // ...
        }
    }

}
