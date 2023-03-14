package com.test.quartzapp.repository;

import com.test.quartzapp.model.vo.JobMstVo;
import com.test.quartzapp.model.vo.JobStepParamVo;
import com.test.quartzapp.model.vo.JobStepVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuartzRepository {

    List<JobMstVo> selectJobList();
    JobMstVo selectJobMaster(String jobRid);
    JobStepVo selectJobStep(String jobRid);
    JobStepParamVo selectJobStepParam(String jobStepRid);

    void updateJobStatus(JobMstVo vo);
}
