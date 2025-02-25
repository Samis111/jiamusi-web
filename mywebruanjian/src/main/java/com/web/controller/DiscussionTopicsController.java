package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.DTO.StudentRepliesAndTopics;
import com.web.domain.DiscussionReplies;
import com.web.domain.DiscussionTopics;

import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.service.DiscussionRepliesService;
import com.web.service.DiscussionTopicsService;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("interaction")
public class DiscussionTopicsController {


    @Autowired
    private DiscussionTopicsService discussionTopicsService;

    @Autowired
    private DiscussionRepliesService discussionRepliesService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("detail/{id}")
    public Result detail(@PathVariable("id") Integer id) {

        DiscussionTopics byId = discussionTopicsService.getById(id);


        QueryWrapper<DiscussionReplies> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id", id);
        List<DiscussionReplies> list = discussionRepliesService.list(queryWrapper);
        byId.setNumber(list.size());
        for (DiscussionReplies discussionReplies : list) {
            UserInfo userInfoServiceById1 = userInfoService.getById(discussionReplies.getReplyCreatorId());
            discussionReplies.setUsername(userInfoServiceById1.getUsername());
        }
        StudentRepliesAndTopics studentRepliesAndTopics = new StudentRepliesAndTopics(byId, list);




        return Result.ok(studentRepliesAndTopics);

    }

    @GetMapping("list")
    public Result<List<DiscussionTopics>> list(DiscussionTopics userInfo) {
        QueryWrapper<DiscussionTopics> queryWrapper = new QueryWrapper<>();

        if (userInfo.getTopicCreatorId() != null && userInfo.getTopicCreatorId() != 0) {
            queryWrapper.eq("reply_creator_id", userInfo.getTopicCreatorId());
        }

        List<DiscussionTopics> list = discussionTopicsService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<DiscussionTopics> find(@PathVariable("id") Integer id) {

        DiscussionTopics byId = discussionTopicsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("create")
    public Result<?> save(@RequestBody DiscussionTopics userInfo) {
        userInfo.setTopicCreateTime(new Date());
        boolean save = discussionTopicsService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody DiscussionTopics userInfo) {

        boolean save = discussionTopicsService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("delete/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = discussionTopicsService.removeById(id);
        return Result.ok();
    }

}
