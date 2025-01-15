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
@RequestMapping("exercise")
public class ExerciseQuestionsController {


    @Autowired
    private ExerciseQuestionsService exerciseQuestionsService;


    @GetMapping("list")
    public Result<List<ExerciseQuestions>> list(ExerciseQuestions exerciseQuestions) {
        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();

        if (exerciseQuestions.getQuestionContent() != null && !exerciseQuestions.getQuestionContent().equals("")) {
            queryWrapper.like("question_content", exerciseQuestions.getQuestionContent());
        }

        if (exerciseQuestions.getQuestionTypeId() != null && exerciseQuestions.getQuestionTypeId() != 0) {
            queryWrapper.eq("question_type_id", exerciseQuestions.getQuestionTypeId());
        }
        queryWrapper.eq("status", "1");
        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<ExerciseQuestions> find(@PathVariable("id") Integer id) {

        ExerciseQuestions byId = exerciseQuestionsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("create")
    public Result<?> save(@RequestBody ExerciseQuestions exerciseQuestions) {

        boolean save = exerciseQuestionsService.save(exerciseQuestions);
        return Result.ok();
    }

    @PutMapping("publish/{id}")
    public Result<?> publish(@PathVariable("id") Integer id) {

        ExerciseQuestions byId = exerciseQuestionsService.getById(id);

        if (byId.getStatus() == 0) {
            byId.setStatus(1);
            exerciseQuestionsService.updateById(byId);
        } else if (byId.getStatus() == 1) {
            byId.setStatus(0);
            exerciseQuestionsService.updateById(byId);
        }

        return Result.ok();
    }

    @PostMapping("update")
    public Result<?> update(@RequestBody ExerciseQuestions userInfo) {

        boolean save = exerciseQuestionsService.updateById(userInfo);
        return Result.ok();
    }

    @DeleteMapping("delete/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = exerciseQuestionsService.removeById(id);
        return Result.ok();
    }

}
