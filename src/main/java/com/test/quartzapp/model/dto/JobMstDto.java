package com.test.quartzapp.model.dto;

import com.test.quartzapp.model.vo.JobStepVo;
import lombok.*;

import java.util.List;

/**
 * Job 마스터 VO
 *
 * 연관 테이블: JOB_MST
 */

public class JobMstDto {

    @Getter
    @Setter
    @ToString
    public static class JobMstReqDto {
        private String rid;
        private String createBy;
        private String createDate;
        private String modifyBy;
        private String modifyDate;
        private String flag;
        private String jobName;
        private String jobDesc;
        private String jobStatus;
        private String scheduleExp;
        private String scheduleExpDesc;
        private String resultCd;
//        private String jobClassFullPath;
        private JobStepVo jobStep;
    }

    @Getter
    @Setter
    @ToString
    public static class JobMstResDto {
        private String rid;
        private String createBy;
        private String createDate;
        private String modifyBy;
        private String modifyDate;
        private String flag;
        private String jobName;
        private String jobDesc;
        private String jobStatus;
        private String scheduleExp;
        private String scheduleExpDesc;
        private String resultCd;
//        private String jobClassFullPath;
        private JobStepVo jobStep;
    }
}
