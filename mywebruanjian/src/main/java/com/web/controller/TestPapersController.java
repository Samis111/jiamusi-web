package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.TestPapers;
import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.service.TestPapersService;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("TestPaper")
public class TestPapersController {


    @Autowired
    private TestPapersService testPapersService;


    @GetMapping
    public Result<List<TestPapers>> list(TestPapers userInfo) {
        QueryWrapper<TestPapers> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<TestPapers> list = testPapersService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<TestPapers> find(@PathVariable("id") Integer id) {

        TestPapers byId = testPapersService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody TestPapers userInfo) {

        boolean save = testPapersService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody TestPapers userInfo) {

        boolean save = testPapersService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = testPapersService.removeById(id);
        return Result.ok();
    }

}
