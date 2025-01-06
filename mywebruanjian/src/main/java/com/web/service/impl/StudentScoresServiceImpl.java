package com.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.domain.StudentScores;
import com.web.service.StudentScoresService;
import com.web.mapper.StudentScoresMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class StudentScoresServiceImpl extends ServiceImpl<StudentScoresMapper, StudentScores>
    implements StudentScoresService{

}




