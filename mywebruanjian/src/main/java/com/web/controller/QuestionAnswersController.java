package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.QuestionAnswer;
import com.web.domain.StudentAnswers;
import com.web.domain.common.Result;
import com.web.service.QuestionAnswerService;
import com.web.service.StudentAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("QuestionAnswers")
public class QuestionAnswersController {


    @Autowired
    private QuestionAnswerService questionAnswerService;


    @GetMapping
    public Result<List<QuestionAnswer>> list(QuestionAnswer userInfo) {
        QueryWrapper<QuestionAnswer> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<QuestionAnswer> list = questionAnswerService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<QuestionAnswer> find(@PathVariable("id") Integer id) {

        QuestionAnswer byId = questionAnswerService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody QuestionAnswer userInfo) {

        boolean save = questionAnswerService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody QuestionAnswer userInfo) {

        boolean save = questionAnswerService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = questionAnswerService.removeById(id);
        return Result.ok();
    }

}
