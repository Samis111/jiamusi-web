package com.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName exercise_questions
 */
@TableName(value ="exercise_questions")
@Data
public class ExerciseQuestions implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer questionId;

    /**
     * 
     */
    private String questionContent;

    /**
     * 
     */
    private Integer questionTypeId;

    /**
     * 
     */
    private Integer questionCreatorId;

    /**
     * 
     */
    private Date questionCreateTime;

    private Integer status;

    private Integer questionCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    private String questionAnswer;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ExerciseQuestions other = (ExerciseQuestions) that;
        return (this.getQuestionId() == null ? other.getQuestionId() == null : this.getQuestionId().equals(other.getQuestionId()))
            && (this.getQuestionContent() == null ? other.getQuestionContent() == null : this.getQuestionContent().equals(other.getQuestionContent()))
            && (this.getQuestionTypeId() == null ? other.getQuestionTypeId() == null : this.getQuestionTypeId().equals(other.getQuestionTypeId()))
            && (this.getQuestionCreatorId() == null ? other.getQuestionCreatorId() == null : this.getQuestionCreatorId().equals(other.getQuestionCreatorId()))
            && (this.getQuestionCreateTime() == null ? other.getQuestionCreateTime() == null : this.getQuestionCreateTime().equals(other.getQuestionCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getQuestionId() == null) ? 0 : getQuestionId().hashCode());
        result = prime * result + ((getQuestionContent() == null) ? 0 : getQuestionContent().hashCode());
        result = prime * result + ((getQuestionTypeId() == null) ? 0 : getQuestionTypeId().hashCode());
        result = prime * result + ((getQuestionCreatorId() == null) ? 0 : getQuestionCreatorId().hashCode());
        result = prime * result + ((getQuestionCreateTime() == null) ? 0 : getQuestionCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", questionId=").append(questionId);
        sb.append(", questionContent=").append(questionContent);
        sb.append(", questionTypeId=").append(questionTypeId);
        sb.append(", questionCreatorId=").append(questionCreatorId);
        sb.append(", questionCreateTime=").append(questionCreateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}