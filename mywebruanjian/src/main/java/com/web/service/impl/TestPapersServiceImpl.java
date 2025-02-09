package com.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.web.domain.DTO.StudentTestPapers;
import com.web.domain.StudentScores;
import com.web.domain.TestPapers;
import com.web.service.TestPapersService;
import com.web.mapper.TestPapersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
@Service
public class TestPapersServiceImpl extends ServiceImpl<TestPapersMapper, TestPapers>
        implements TestPapersService {

    @Autowired
    private TestPapersMapper testPapersMapper;

    @Autowired
    private StudentScoresServiceImpl studentScoresService;

    @Override
    public List<StudentTestPapers> joinScores(Integer id) {

        MPJLambdaWrapper<TestPapers> mpjLambdaWrapper = new MPJLambdaWrapper<TestPapers>();

        mpjLambdaWrapper.selectAll(StudentTestPapers.class).selectAll(StudentScores.class)
                .leftJoin(StudentScores.class, StudentScores::getPaperId, StudentScores::getPaperId);


        List<StudentTestPapers> studentScores = testPapersMapper.selectJoinList(StudentTestPapers.class, mpjLambdaWrapper);

        List<StudentTestPapers> testPapers = new ArrayList<>();
        for (StudentTestPapers studentTestPapers : studentScores) {

            if (studentTestPapers.getStudentId() == null) {
                studentTestPapers.setNewStatus(0);
                testPapers.add(studentTestPapers);
                continue;
            }

            if (studentTestPapers.getStudentId().equals(id)) {
                studentTestPapers.setNewStatus(1);
                testPapers.add(studentTestPapers);
            }
        }

        return testPapers;
    }
}




