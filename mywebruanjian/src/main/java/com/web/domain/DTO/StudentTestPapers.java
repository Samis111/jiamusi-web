package com.web.domain.DTO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.domain.ExerciseQuestions;
import com.web.domain.TestPapers;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentTestPapers extends TestPapers {

    public String totalScore;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer paperId;

    /**
     *
     */
    private String paperName;

    /**
     *
     */
    private Integer paperCreatorId;

    private String paperNode;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date paperCreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date starttime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endtime;

    private Integer status;



    @TableField(exist = false)
    private Integer newStatus;



    @TableField(exist = false)
    private List<ExerciseQuestions> questions;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer scoreId;

    /**
     *
     */
    private Integer studentId;


    /**
     *
     */
    private Date testTime;

}
