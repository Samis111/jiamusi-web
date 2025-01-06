package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.StudentScores;
import com.web.domain.TestPapers;
import com.web.domain.common.Result;
import com.web.service.StudentScoresService;
import com.web.service.TestPapersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("StudentScores")
public class StudentScoresController {


    @Autowired
    private StudentScoresService studentScoresService;


    @GetMapping
    public Result<List<StudentScores>> list(StudentScores userInfo) {
        QueryWrapper<StudentScores> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<StudentScores> list = studentScoresService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<StudentScores> find(@PathVariable("id") Integer id) {

        StudentScores byId = studentScoresService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody StudentScores userInfo) {

        boolean save = studentScoresService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody StudentScores userInfo) {

        boolean save = studentScoresService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = studentScoresService.removeById(id);
        return Result.ok();
    }

}
