package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.PaperQuestionRelation;
import com.web.domain.QuestionAnswer;
import com.web.domain.common.Result;
import com.web.service.PaperQuestionRelationService;
import com.web.service.QuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("PaperQuestionRelation")
public class PaperQuestionRelationController {


    @Autowired
    private PaperQuestionRelationService paperQuestionRelationService;


    @GetMapping
    public Result<List<PaperQuestionRelation>> list(PaperQuestionRelation userInfo) {
        QueryWrapper<PaperQuestionRelation> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<PaperQuestionRelation> list = paperQuestionRelationService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<PaperQuestionRelation> find(@PathVariable("id") Integer id) {

        PaperQuestionRelation byId = paperQuestionRelationService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody PaperQuestionRelation userInfo) {

        boolean save = paperQuestionRelationService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody PaperQuestionRelation userInfo) {

        boolean save = paperQuestionRelationService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = paperQuestionRelationService.removeById(id);
        return Result.ok();
    }

}
