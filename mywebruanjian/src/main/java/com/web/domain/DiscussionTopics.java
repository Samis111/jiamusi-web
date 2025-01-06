package com.web.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName discussion_topics
 */
@TableName(value ="discussion_topics")
@Data
public class DiscussionTopics implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer topicId;

    /**
     * 
     */
    private String topicTitle;

    /**
     * 
     */
    private Integer topicCreatorId;

    /**
     * 
     */
    private Date topicCreateTime;

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
        DiscussionTopics other = (DiscussionTopics) that;
        return (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()))
            && (this.getTopicTitle() == null ? other.getTopicTitle() == null : this.getTopicTitle().equals(other.getTopicTitle()))
            && (this.getTopicCreatorId() == null ? other.getTopicCreatorId() == null : this.getTopicCreatorId().equals(other.getTopicCreatorId()))
            && (this.getTopicCreateTime() == null ? other.getTopicCreateTime() == null : this.getTopicCreateTime().equals(other.getTopicCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        result = prime * result + ((getTopicTitle() == null) ? 0 : getTopicTitle().hashCode());
        result = prime * result + ((getTopicCreatorId() == null) ? 0 : getTopicCreatorId().hashCode());
        result = prime * result + ((getTopicCreateTime() == null) ? 0 : getTopicCreateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", topicId=").append(topicId);
        sb.append(", topicTitle=").append(topicTitle);
        sb.append(", topicCreatorId=").append(topicCreatorId);
        sb.append(", topicCreateTime=").append(topicCreateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}