package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.MultimediaCourseware;
import com.web.domain.PaperQuestionRelation;
import com.web.domain.common.Result;
import com.web.service.MultimediaCoursewareService;
import com.web.service.PaperQuestionRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Multimedia")
public class MultimediaController {


    @Autowired
    private MultimediaCoursewareService multimediaCoursewareService;


    @GetMapping
    public Result<List<MultimediaCourseware>> list(MultimediaCourseware userInfo) {
        QueryWrapper<MultimediaCourseware> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<MultimediaCourseware> list = multimediaCoursewareService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<MultimediaCourseware> find(@PathVariable("id") Integer id) {

        MultimediaCourseware byId = multimediaCoursewareService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody MultimediaCourseware userInfo) {

        boolean save = multimediaCoursewareService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody MultimediaCourseware userInfo) {

        boolean save = multimediaCoursewareService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = multimediaCoursewareService.removeById(id);
        return Result.ok();
    }

}
