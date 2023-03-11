package com.test.quartzapp.repository;

import com.test.quartzapp.model.dto.JobMstDto;
import com.test.quartzapp.model.vo.JobMstVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuartzRepository {

    List<JobMstVo> selectJobList();

}
