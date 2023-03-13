package com.test.quartzapp.controller;

import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.repository.QuartzRepository;
import com.test.quartzapp.service.QuartzAppService;
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
    void getJobDetailTest() {
        QuartzAppService quartzAppService = new QuartzAppService(quartzRepository, scheduler);
//        String jobRid = "ec894d56d097488cac23c8d7e5305cac"; // Test1
        String jobRid = "ac894d56d097488cac23c8d7e5305cac"; // Test2
//        String jobRid = "bc894d56d097488cac23c8d7e5305cac"; // Test3
//        String jobRid = "cc894d56d097488cac23c8d7e5305cac"; // Test4
        JobMstVo jobDetailVo = quartzAppService.getJobDetail(jobRid);
        System.out.println("jobDetailVo.JobClassFullPath : " + jobDetailVo.getJobClassFullPath());
    }

}