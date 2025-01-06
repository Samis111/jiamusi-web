package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.ExerciseQuestions;
import com.web.domain.ExerciseType;
import com.web.domain.common.Result;
import com.web.service.ExerciseQuestionsService;
import com.web.service.ExerciseTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ExerciseQuestions")
public class ExerciseQuestionsController {


    @Autowired
    private ExerciseQuestionsService exerciseQuestionsService;


    @GetMapping
    public Result<List<ExerciseQuestions>> list(ExerciseType userInfo) {
        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<ExerciseQuestions> list = exerciseQuestionsService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<ExerciseQuestions> find(@PathVariable("id") Integer id) {

        ExerciseQuestions byId = exerciseQuestionsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody ExerciseQuestions userInfo) {

        boolean save = exerciseQuestionsService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody ExerciseQuestions userInfo) {

        boolean save = exerciseQuestionsService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = exerciseQuestionsService.removeById(id);
        return Result.ok();
    }

}
