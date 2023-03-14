package com.test.quartzapp.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobStepVo {

    private String rid;
    private String createBy;
    private String createDate;
    private String modifyBy;
    private String modifyDate;
    private String flag;
    private String stepSeq;
    private String stepName;
    private String stepDesc;
    private String stepType;
    private String ridJobMst;
    private JobStepParamVo jobStepParam;

}
