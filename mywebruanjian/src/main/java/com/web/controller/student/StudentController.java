package com.web.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.*;
import com.web.domain.DTO.StudentRepliesAndTopics;
import com.web.domain.common.Result;
import com.web.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private TestPapersService exerciseQuestionsService;

    @Autowired
    private DiscussionTopicsService discussionTopicsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private DiscussionRepliesService discussionRepliesService;


    @GetMapping("/exercise/list")
    public Result<List<TestPapers>> exerciseList(@RequestParam("keyword") String keyword) {
        QueryWrapper<TestPapers> queryWrapper = new QueryWrapper<>();
        List<TestPapers> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("/test/list")
    public Result<List<TestPapers>> textList(@RequestParam("keyword") String keyword) {
        QueryWrapper<TestPapers> queryWrapper = new QueryWrapper<>();
        List<TestPapers> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("/interaction/list")
    public Result<List<DiscussionTopics>> interactionList(@RequestParam("keyword") String keyword) {
        QueryWrapper<DiscussionTopics> queryWrapper = new QueryWrapper<>();
        List<DiscussionTopics> list = discussionTopicsService.list(queryWrapper);
        for (DiscussionTopics discussionTopics : list) {
            UserInfo userInfoServiceById = userInfoService.getById(discussionTopics.getTopicCreatorId());
            discussionTopics.setCreator(userInfoServiceById.getUsername());
        }
        return Result.ok(list);
    }


    @GetMapping("/interaction/detail/{id}")
    public Result<StudentRepliesAndTopics> detail(@PathVariable("id") String id) {

        DiscussionTopics byId = discussionTopicsService.getById(id);
        UserInfo userInfoServiceById = userInfoService.getById(byId.getTopicCreatorId());
        byId.setCreator(userInfoServiceById.getUsername());
        QueryWrapper<DiscussionReplies> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id", id);
        List<DiscussionReplies> list = discussionRepliesService.list(queryWrapper);
        StudentRepliesAndTopics studentRepliesAndTopics = new StudentRepliesAndTopics(byId, list);

        return Result.ok(studentRepliesAndTopics);
    }


    @RequestMapping("/interaction/reply")
    public Result<?> reply(@RequestBody DiscussionReplies discussionReplies) {
        boolean save = discussionRepliesService.save(discussionReplies);
        return Result.ok();
    }


    @RequestMapping("/interaction/create")
    public Result<?> create(@RequestBody DiscussionTopics discussionTopics) {
        boolean save = discussionTopicsService.save(discussionTopics);
        return Result.ok();
    }

}
