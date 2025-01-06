package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.ExerciseType;
import com.web.domain.MultimediaCourseware;
import com.web.domain.common.Result;
import com.web.service.ExerciseTypeService;
import com.web.service.MultimediaCoursewareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ExerciseType")
public class ExerciseTypeController {


    @Autowired
    private ExerciseTypeService exerciseTypeService;


    @GetMapping
    public Result<List<ExerciseType>> list(ExerciseType userInfo) {
        QueryWrapper<ExerciseType> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<ExerciseType> list = exerciseTypeService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<ExerciseType> find(@PathVariable("id") Integer id) {

        ExerciseType byId = exerciseTypeService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody ExerciseType userInfo) {

        boolean save = exerciseTypeService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody ExerciseType userInfo) {

        boolean save = exerciseTypeService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = exerciseTypeService.removeById(id);
        return Result.ok();
    }

}
