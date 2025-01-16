package com.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName student_answers
 */
@TableName(value ="student_answers")
@Data
public class StudentAnswers implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer answerId;

    /**
     * 
     */
    private Integer studentId;

    /**
     * 
     */
    private Integer paperId;

    /**
     * 
     */
    private Integer questionId;

    /**
     * 
     */
    private String studentAnswerContent;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date answerTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

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
        StudentAnswers other = (StudentAnswers) that;
        return (this.getAnswerId() == null ? other.getAnswerId() == null : this.getAnswerId().equals(other.getAnswerId()))
            && (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
            && (this.getPaperId() == null ? other.getPaperId() == null : this.getPaperId().equals(other.getPaperId()))
            && (this.getQuestionId() == null ? other.getQuestionId() == null : this.getQuestionId().equals(other.getQuestionId()))
            && (this.getStudentAnswerContent() == null ? other.getStudentAnswerContent() == null : this.getStudentAnswerContent().equals(other.getStudentAnswerContent()))
            && (this.getAnswerTime() == null ? other.getAnswerTime() == null : this.getAnswerTime().equals(other.getAnswerTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAnswerId() == null) ? 0 : getAnswerId().hashCode());
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getPaperId() == null) ? 0 : getPaperId().hashCode());
        result = prime * result + ((getQuestionId() == null) ? 0 : getQuestionId().hashCode());
        result = prime * result + ((getStudentAnswerContent() == null) ? 0 : getStudentAnswerContent().hashCode());
        result = prime * result + ((getAnswerTime() == null) ? 0 : getAnswerTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", answerId=").append(answerId);
        sb.append(", studentId=").append(studentId);
        sb.append(", paperId=").append(paperId);
        sb.append(", questionId=").append(questionId);
        sb.append(", studentAnswerContent=").append(studentAnswerContent);
        sb.append(", answerTime=").append(answerTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}