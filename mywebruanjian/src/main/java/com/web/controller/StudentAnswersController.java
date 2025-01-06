package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.StudentAnswers;
import com.web.domain.StudentScores;
import com.web.domain.common.Result;
import com.web.service.StudentAnswersService;
import com.web.service.StudentScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("StudentAnswers")
public class StudentAnswersController {


    @Autowired
    private StudentAnswersService studentAnswersService;


    @GetMapping
    public Result<List<StudentAnswers>> list(StudentAnswers userInfo) {
        QueryWrapper<StudentAnswers> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<StudentAnswers> list = studentAnswersService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<StudentAnswers> find(@PathVariable("id") Integer id) {

        StudentAnswers byId = studentAnswersService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody StudentAnswers userInfo) {

        boolean save = studentAnswersService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody StudentAnswers userInfo) {

        boolean save = studentAnswersService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = studentAnswersService.removeById(id);
        return Result.ok();
    }

}
