package com.web.service;

import com.web.domain.StudentScores;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface StudentScoresService extends IService<StudentScores> {

    Integer getMax(Integer paperId);

    Integer getMin(Integer paperId);

    Integer getAvg(Integer paperId);

    Integer getCount(Integer paperId);

    Integer getAAndB(Integer max ,Integer min,  Integer paperId);
}
