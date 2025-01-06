package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.DiscussionReplies;
import com.web.domain.DiscussionTopics;
import com.web.domain.ExerciseType;
import com.web.domain.common.Result;
import com.web.service.DiscussionRepliesService;
import com.web.service.DiscussionTopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("DiscussionReplies")
public class DiscussionRepliesController {


    @Autowired
    private DiscussionRepliesService discussionRepliesService;


    @GetMapping
    public Result<List<DiscussionReplies>> list(DiscussionReplies userInfo) {
        QueryWrapper<DiscussionReplies> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<DiscussionReplies> list = discussionRepliesService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<DiscussionReplies> find(@PathVariable("id") Integer id) {

        DiscussionReplies byId = discussionRepliesService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody DiscussionReplies userInfo) {

        boolean save = discussionRepliesService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody DiscussionReplies userInfo) {

        boolean save = discussionRepliesService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = discussionRepliesService.removeById(id);
        return Result.ok();
    }

}
