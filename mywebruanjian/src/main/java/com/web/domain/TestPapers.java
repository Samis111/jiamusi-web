package com.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName test_papers
 */
@TableName(value ="test_papers")
@Data
public class TestPapers implements Serializable {
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

    private String totalScore;

    @TableField(exist = false)
    private Integer newStatus;



    @TableField(exist = false)
    private List<ExerciseQuestions> questions;

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
        TestPapers other = (TestPapers) that;
        return (this.getPaperId() == null ? other.getPaperId() == null : this.getPaperId().equals(other.getPaperId()))
            && (this.getPaperName() == null ? other.getPaperName() == null : this.getPaperName().equals(other.getPaperName()))
            && (this.getPaperCreatorId() == null ? other.getPaperCreatorId() == null : this.getPaperCreatorId().equals(other.getPaperCreatorId()))
            && (this.getPaperCreateTime() == null ? other.getPaperCreateTime() == null : this.getPaperCreateTime().equals(other.getPaperCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPaperId() == null) ? 0 : getPaperId().hashCode());
        result = prime * result + ((getPaperName() == null) ? 0 : getPaperName().hashCode());
        result = prime * result + ((getPaperCreatorId() == null) ? 0 : getPaperCreatorId().hashCode());
        result = prime * result + ((getPaperCreateTime() == null) ? 0 : getPaperCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", paperId=").append(paperId);
        sb.append(", paperName=").append(paperName);
        sb.append(", paperCreatorId=").append(paperCreatorId);
        sb.append(", paperCreateTime=").append(paperCreateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}