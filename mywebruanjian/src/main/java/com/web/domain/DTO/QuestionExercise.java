package com.web.domain.DTO;

import lombok.Data;

import java.util.List;

@Data
public class QuestionExercise {


    private String answer;
    private String blankCount;
    private String content;
    private List<Options> options;
    private Integer questionTypeId;
    private Integer score;

    private Integer questionCreatorId;

    private Integer newquestionId;
}
