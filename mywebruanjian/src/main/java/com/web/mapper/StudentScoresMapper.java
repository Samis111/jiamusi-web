package com.web.mapper;

import com.web.domain.StudentScores;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Entity com.web.domain.StudentScores
 */
public interface StudentScoresMapper extends BaseMapper<StudentScores> {
    @Select("SELECT AVG(total_score) FROM student_scores WHERE  paper_id =#{paperId}")
    Integer getAvg(Integer paperId);
    @Select("SELECT MAX(total_score) FROM student_scores WHERE  paper_id =#{paperId}")
    Integer getMax(Integer paperId);

    @Select("SELECT MIN(total_score) FROM student_scores WHERE  paper_id =#{paperId}")
    Integer getMin( Integer paperId);

    @Select("SELECT COUNT(*) FROM student_scores WHERE  paper_id =#{paperId}")
    Integer getCount( Integer paperId);

    @Select("SELECT count(DISTINCT student_id) FROM   student_scores WHERE  ( paper_id =#{paperId} AND (total_score<=#{max}  && #{min}<= total_score))")
    Integer getAAndB(Integer max ,Integer min,  Integer paperId);
}




