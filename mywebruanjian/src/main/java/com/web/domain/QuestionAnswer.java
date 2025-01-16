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
 * @TableName question_answer
 */
@TableName(value ="question_answer")
@Data
public class QuestionAnswer implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer qaId;

    /**
     * 
     */
    private String questionTitle;

    /**
     * 
     */
    private String questionContent;

    /**
     * 
     */
    private Integer questionCreatorId;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date questionCreateTime;

    /**
     * 
     */
    private String answerContent;

    /**
     * 
     */
    private Integer answerCreatorId;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date answerCreateTime;

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
        QuestionAnswer other = (QuestionAnswer) that;
        return (this.getQaId() == null ? other.getQaId() == null : this.getQaId().equals(other.getQaId()))
            && (this.getQuestionTitle() == null ? other.getQuestionTitle() == null : this.getQuestionTitle().equals(other.getQuestionTitle()))
            && (this.getQuestionContent() == null ? other.getQuestionContent() == null : this.getQuestionContent().equals(other.getQuestionContent()))
            && (this.getQuestionCreatorId() == null ? other.getQuestionCreatorId() == null : this.getQuestionCreatorId().equals(other.getQuestionCreatorId()))
            && (this.getQuestionCreateTime() == null ? other.getQuestionCreateTime() == null : this.getQuestionCreateTime().equals(other.getQuestionCreateTime()))
            && (this.getAnswerContent() == null ? other.getAnswerContent() == null : this.getAnswerContent().equals(other.getAnswerContent()))
            && (this.getAnswerCreatorId() == null ? other.getAnswerCreatorId() == null : this.getAnswerCreatorId().equals(other.getAnswerCreatorId()))
            && (this.getAnswerCreateTime() == null ? other.getAnswerCreateTime() == null : this.getAnswerCreateTime().equals(other.getAnswerCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getQaId() == null) ? 0 : getQaId().hashCode());
        result = prime * result + ((getQuestionTitle() == null) ? 0 : getQuestionTitle().hashCode());
        result = prime * result + ((getQuestionContent() == null) ? 0 : getQuestionContent().hashCode());
        result = prime * result + ((getQuestionCreatorId() == null) ? 0 : getQuestionCreatorId().hashCode());
        result = prime * result + ((getQuestionCreateTime() == null) ? 0 : getQuestionCreateTime().hashCode());
        result = prime * result + ((getAnswerContent() == null) ? 0 : getAnswerContent().hashCode());
        result = prime * result + ((getAnswerCreatorId() == null) ? 0 : getAnswerCreatorId().hashCode());
        result = prime * result + ((getAnswerCreateTime() == null) ? 0 : getAnswerCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", qaId=").append(qaId);
        sb.append(", questionTitle=").append(questionTitle);
        sb.append(", questionContent=").append(questionContent);
        sb.append(", questionCreatorId=").append(questionCreatorId);
        sb.append(", questionCreateTime=").append(questionCreateTime);
        sb.append(", answerContent=").append(answerContent);
        sb.append(", answerCreatorId=").append(answerCreatorId);
        sb.append(", answerCreateTime=").append(answerCreateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}