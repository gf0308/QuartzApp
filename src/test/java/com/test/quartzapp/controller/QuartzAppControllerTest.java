package com.test.quartzapp.controller;

import com.test.quartzapp.constant.JobConstants;
import com.test.quartzapp.exception.JobException;
import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobStepVo;
import com.test.quartzapp.repository.QuartzRepository;
import com.test.quartzapp.service.QuartzAppService;
import com.test.quartzapp.service.QuartzAppServiceTest;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuartzAppControllerTest {

    @Autowired
    private QuartzRepository quartzRepository;
    @Autowired
    private Scheduler scheduler;

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

    @Test
    void getJobDetailTest() {
        QuartzAppServiceTest quartzAppServiceTest = new QuartzAppServiceTest(quartzRepository, scheduler);
        String jobRid = "ec894d56d097488cac23c8d7e5305cac"; // 배치 1 rid

        JobMstVo jobMstVo = quartzAppServiceTest.getJobDetail(jobRid);

//        quartzAppServiceTest.runJob();
        // jobDetailVo.getJobStepType() 을얻고
        // jobDetailVo.getJobStepParam() 을 얻어
        // 분기처리를 하고, 파라미터 투입해 실제기능실행을 하도록 수정해야 함.
        // jobDetailVo 를 얻는다 (jobDetailVo -> jobMstVo 로 명명 변경하도록 한다)
        // jobDetailVo(jobMstVo)안의 jobStepVo에서 jobStepVo.getJobStepType() 을 보고 잡스텝의 타입을 알아낸다
        // 그리고 jobStepVo 안의 jobStepParamVo 을 보고 jobStepParamVo.getParamContent() 를 호출해서 파라미터값을 얻는다.

        // jobStepType : "RUN JAR" 일 때
        // =>
        // String paramJarFullPath = jobStepParamVo.getParamContent();
        // String jarRunCommand = "java -jar " + paramJarFullPath ;
        // JOB 클래스객체의 JobExecutionContext 에 넣어서 보내기





//
//
////        QuartzAppService quartzAppService = new QuartzAppServiceTest(quartzRepository, scheduler);
////        String jobRid = "ec894d56d097488cac23c8d7e5305cac"; // Test1
//        String jobRid = "ac894d56d097488cac23c8d7e5305cac"; // Test2
////        String jobRid = "bc894d56d097488cac23c8d7e5305cac"; // Test3
////        String jobRid = "cc894d56d097488cac23c8d7e5305cac"; // Test4
//        JobMstVo jobDetailVo = quartzAppService.getJobDetail(jobRid);
//        System.out.println("jobDetailVo.JobClassFullPath : " + jobDetailVo.getJobClassFullPath());
    }

}