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
 * @TableName multimedia_courseware
 */
@TableName(value ="multimedia_courseware")
@Data
public class MultimediaCourseware implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer coursewareId;

    /**
     * 
     */
    private String coursewareName;

    /**
     * 
     */
    private Integer coursewareTypeId;

    /**
     * 
     */
    private String coursewarePath;

    /**
     * 
     */
    private Integer coursewareCreatorId;

    @TableField(exist = false)
    private String creatorName;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date coursewareCreateTime;



    private String size;

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
        MultimediaCourseware other = (MultimediaCourseware) that;
        return (this.getCoursewareId() == null ? other.getCoursewareId() == null : this.getCoursewareId().equals(other.getCoursewareId()))
            && (this.getCoursewareName() == null ? other.getCoursewareName() == null : this.getCoursewareName().equals(other.getCoursewareName()))
            && (this.getCoursewareTypeId() == null ? other.getCoursewareTypeId() == null : this.getCoursewareTypeId().equals(other.getCoursewareTypeId()))
            && (this.getCoursewarePath() == null ? other.getCoursewarePath() == null : this.getCoursewarePath().equals(other.getCoursewarePath()))
            && (this.getCoursewareCreatorId() == null ? other.getCoursewareCreatorId() == null : this.getCoursewareCreatorId().equals(other.getCoursewareCreatorId()))
            && (this.getCoursewareCreateTime() == null ? other.getCoursewareCreateTime() == null : this.getCoursewareCreateTime().equals(other.getCoursewareCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCoursewareId() == null) ? 0 : getCoursewareId().hashCode());
        result = prime * result + ((getCoursewareName() == null) ? 0 : getCoursewareName().hashCode());
        result = prime * result + ((getCoursewareTypeId() == null) ? 0 : getCoursewareTypeId().hashCode());
        result = prime * result + ((getCoursewarePath() == null) ? 0 : getCoursewarePath().hashCode());
        result = prime * result + ((getCoursewareCreatorId() == null) ? 0 : getCoursewareCreatorId().hashCode());
        result = prime * result + ((getCoursewareCreateTime() == null) ? 0 : getCoursewareCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", coursewareId=").append(coursewareId);
        sb.append(", coursewareName=").append(coursewareName);
        sb.append(", coursewareTypeId=").append(coursewareTypeId);
        sb.append(", coursewarePath=").append(coursewarePath);
        sb.append(", coursewareCreatorId=").append(coursewareCreatorId);
        sb.append(", coursewareCreateTime=").append(coursewareCreateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}