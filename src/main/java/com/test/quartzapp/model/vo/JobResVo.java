package com.test.quartzapp.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Job 응답 VO
 *
 * 연관 테이블:
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobResVo {

    private String jobProcessResult;    // 처리 결과
    private String jobRid;              // 처리대상 job rid
    private String processDateTime;     // 처리 수행 일시

}
