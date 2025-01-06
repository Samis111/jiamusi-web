package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.domain.common.ResultCodeEnum;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("auto")
@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("login")
    public Result login(@RequestBody UserInfo userInfo) {

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
            queryWrapper.eq("username", userInfo.getUsername());
        }
        if (userInfo.getPassword() != null && !userInfo.getPassword().equals("")) {
            queryWrapper.eq("password", userInfo.getPassword());
        }

        List<UserInfo> list = userInfoService.list(queryWrapper);
        if (list.size() == 1) {
            return Result.ok(list.get(0));

        } else {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }


    }


}
