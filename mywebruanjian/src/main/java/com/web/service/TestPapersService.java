package com.web.service;

import com.web.domain.DTO.StudentTestPapers;
import com.web.domain.TestPapers;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface TestPapersService extends IService<TestPapers> {

    List<StudentTestPapers>  joinScores(Integer id);
}
