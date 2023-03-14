package com.test.secondApp.scheduler;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.core.QuartzScheduler;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@SpringBootTest(classes = com.test.quartzapp.conf.JobSetting.class)
//@RequiredArgsConstructor
public class MainTest {
//    private final Scheduler scheduler;

    @Test
    void test() throws SchedulerException, InterruptedException {
//        Scheduler scheduler = new QuartzScheduler()
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = stdSchedulerFactory.getScheduler();

        if(true) {// stepType : "RUN JAR" 인 케이스
            // 사용할 JOB 클래스: JarRunJob.class
            Class JobClassObject = JarRunJob.class;

            String paramContent = "C:\\schedule_test\\jars\\TestJar1.jar"; // 어디로부턴가 받아온 파라미터값, : "C:\schedule_test\jars\TestJar1.jar"
            String scheduleExp = "0/5 * * * * ?";                          // 어딘가로부터 받아온 스케줄표현식 값 : "0/5 * * * * ?"
            String jobRid = "ec894d56d097488cac23c8d7e5305cac";                 // 어딘가로부터 받아온 jobRid: "ec894d56d097488cac23c8d7e5305cac"

            // jobDataMap 재료 작성
            String runJarCommand = "java -jar " + paramContent;
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("runJarCommand", runJarCommand);

            JobDetail jobDetail = JobSetting.buildJobDetail(JobClassObject, hashMap, jobRid); // jobDetail 빌드
            Trigger trigger = JobSetting.buildJobTrigger(scheduleExp); // JobTrigger 생성

            // 스케줄러에서 스케줄 실행
            Date date = scheduler.scheduleJob(jobDetail, trigger);

            System.out.println("스케줄 정상 실행 / 실행시각: " + date);

            Thread.sleep(10000);

        } else if(false) {
            // ...
        }

    }



}
