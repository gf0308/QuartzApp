package com.test.quartzapp.model.vo;

import lombok.*;

/**
 * Job 마스터 VO
 *
 * 연관 테이블: JOB_MST
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobMstVo {

    private String rid;
    private String createBy;
    private String createDate;
    private String modifyBy;
    private String modifyDate;
    private String flag;
    private String jobName;
    private String jobDesc;
    private String status;
    private String scheduleExp;
    private String scheduleExpDesc;
    private String runResult;
    private String jobClassFullPath;

}
