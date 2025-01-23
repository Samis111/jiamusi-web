package com.web.domain.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.domain.DiscussionReplies;
import com.web.domain.DiscussionTopics;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRepliesAndTopics {


    private Integer topicId;

    private String topicTitle;

    private Integer topicCreatorId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date topicCreateTime;
    private Integer number;
    private String status;

    private String creator;

    private List<DiscussionReplies> replies;

    public StudentRepliesAndTopics(DiscussionTopics discussionTopics, List<DiscussionReplies> replies) {
        this.topicId = discussionTopics.getTopicId();
        this.topicTitle = discussionTopics.getTopicTitle();
        this.topicCreatorId = discussionTopics.getTopicCreatorId();
        this.topicCreateTime = discussionTopics.getTopicCreateTime();
        this.number = discussionTopics.getNumber();
        this.status = discussionTopics.getStatus();
        this.creator=discussionTopics.getCreator();
        this.replies = replies;
    }
}
