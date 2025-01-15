package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserInfoController {


    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("list")
    public Result<List<UserInfo>> list(UserInfo userInfo) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();

        if (userInfo.getKeyword() != null && !userInfo.getKeyword().equals("")) {
            queryWrapper.like("username", userInfo.getKeyword())
            .or().like("email",userInfo.getKeyword());
        }

        if (userInfo.getRole()!=null && !userInfo.getRole().equals("")){
            queryWrapper.eq("role",userInfo.getRole());
        }

        List<UserInfo> list = userInfoService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<UserInfo> find(@PathVariable("id") Integer id) {

        UserInfo byId = userInfoService.getById(id);
        return Result.ok(byId);
    }

    @RequestMapping("create")
    public Result<?> save(@RequestBody UserInfo userInfo) {

        boolean save = userInfoService.save(userInfo);
        return Result.ok();
    }


    @PutMapping("update")
    public Result<?> update(@RequestBody UserInfo userInfo) {

        boolean save = userInfoService.updateById(userInfo);
        return Result.ok();
    }

    @PutMapping("status/{id}")
    public Result<?> status(@PathVariable("id")Integer id,@RequestBody UserInfo userInfo){

        userInfo.setUserId(id);

        userInfoService.updateById(userInfo);
        return Result.ok();
    }

    @PutMapping("reset-password/{id}")
    public Result<?> resetPassword(@PathVariable("id")Integer id){

        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(id);
        userInfo.setPassword("123456");
        userInfoService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = userInfoService.removeById(id);
        return Result.ok();
    }

}
