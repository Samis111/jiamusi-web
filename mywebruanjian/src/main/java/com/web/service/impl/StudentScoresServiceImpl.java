package com.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.domain.StudentScores;
import com.web.service.StudentScoresService;
import com.web.mapper.StudentScoresMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class StudentScoresServiceImpl extends ServiceImpl<StudentScoresMapper, StudentScores>
    implements StudentScoresService{

    @Autowired
    private StudentScoresMapper studentScoresMapper;

    @Override
    public Integer getMax(Integer paperId) {
        return studentScoresMapper.getMax(paperId);
    }

    @Override
    public Integer getMin(Integer paperId) {
        return studentScoresMapper.getMin(paperId);
    }

    @Override
    public Integer getAvg(Integer paperId) {

        return studentScoresMapper.getAvg(paperId);
    }

    @Override
    public Integer getCount(Integer paperId) {
        return studentScoresMapper.getCount(paperId);
    }

    @Override
    public Integer getAAndB(Integer max ,Integer min,  Integer paperId) {
        return studentScoresMapper.getAAndB(max ,min,  paperId);
    }
}




