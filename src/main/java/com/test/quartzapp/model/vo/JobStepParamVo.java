package com.test.quartzapp.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobStepParamVo {

    private String rid;
    private String createBy;
    private String createDate;
    private String modifyBy;
    private String modifyDate;
    private String flag;
    private String paramName;
    private String paramDesc;
    private String paramContent;
    private String ridJobStep;

}