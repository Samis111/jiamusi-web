package com.web.domain.DTO;

import com.web.domain.ExerciseQuestions;
import lombok.Data;

import java.util.List;

@Data
public class TestPapersDTO {


    private Integer userId;

    private Integer paperId;

    private List<ExerciseQuestions> answers;

}
