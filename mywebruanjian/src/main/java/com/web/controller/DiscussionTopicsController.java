package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.DiscussionTopics;
import com.web.domain.ExerciseQuestions;
import com.web.domain.ExerciseType;
import com.web.domain.common.Result;
import com.web.service.DiscussionTopicsService;
import com.web.service.ExerciseQuestionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("DiscussionTopics")
public class DiscussionTopicsController {


    @Autowired
    private DiscussionTopicsService discussionTopicsService;


    @GetMapping
    public Result<List<DiscussionTopics>> list(ExerciseType userInfo) {
        QueryWrapper<DiscussionTopics> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<DiscussionTopics> list = discussionTopicsService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<DiscussionTopics> find(@PathVariable("id") Integer id) {

        DiscussionTopics byId = discussionTopicsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody DiscussionTopics userInfo) {

        boolean save = discussionTopicsService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody DiscussionTopics userInfo) {

        boolean save = discussionTopicsService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = discussionTopicsService.removeById(id);
        return Result.ok();
    }

}
