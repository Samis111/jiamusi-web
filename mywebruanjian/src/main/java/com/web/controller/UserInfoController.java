package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userInfo")
public class UserInfoController {


    @Autowired
    private UserInfoService userInfoService;


    @GetMapping
    public Result<List<UserInfo>> list(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
            queryWrapper.eq("username", userInfo.getUsername());
        }

        List<UserInfo> list = userInfoService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<UserInfo> find(@PathVariable("id") Integer id) {

        UserInfo byId = userInfoService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody UserInfo userInfo) {

        boolean save = userInfoService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody UserInfo userInfo) {

        boolean save = userInfoService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = userInfoService.removeById(id);
        return Result.ok();
    }

}
