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
 * @TableName discussion_replies
 */
@TableName(value ="discussion_replies")
@Data
public class DiscussionReplies implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer replyId;

    /**
     * 
     */
    private String replyContent;

    /**
     * 
     */
    private Integer replyCreatorId;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyCreateTime;

    /**
     * 
     */
    private Integer topicId;

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
        DiscussionReplies other = (DiscussionReplies) that;
        return (this.getReplyId() == null ? other.getReplyId() == null : this.getReplyId().equals(other.getReplyId()))
            && (this.getReplyContent() == null ? other.getReplyContent() == null : this.getReplyContent().equals(other.getReplyContent()))
            && (this.getReplyCreatorId() == null ? other.getReplyCreatorId() == null : this.getReplyCreatorId().equals(other.getReplyCreatorId()))
            && (this.getReplyCreateTime() == null ? other.getReplyCreateTime() == null : this.getReplyCreateTime().equals(other.getReplyCreateTime()))
            && (this.getTopicId() == null ? other.getTopicId() == null : this.getTopicId().equals(other.getTopicId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getReplyId() == null) ? 0 : getReplyId().hashCode());
        result = prime * result + ((getReplyContent() == null) ? 0 : getReplyContent().hashCode());
        result = prime * result + ((getReplyCreatorId() == null) ? 0 : getReplyCreatorId().hashCode());
        result = prime * result + ((getReplyCreateTime() == null) ? 0 : getReplyCreateTime().hashCode());
        result = prime * result + ((getTopicId() == null) ? 0 : getTopicId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", replyId=").append(replyId);
        sb.append(", replyContent=").append(replyContent);
        sb.append(", replyCreatorId=").append(replyCreatorId);
        sb.append(", replyCreateTime=").append(replyCreateTime);
        sb.append(", topicId=").append(topicId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}