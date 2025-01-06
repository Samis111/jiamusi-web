package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.CoursewareType;
import com.web.domain.DiscussionReplies;
import com.web.domain.common.Result;
import com.web.service.CoursewareTypeService;
import com.web.service.DiscussionRepliesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("CoursewareType")
public class CoursewareTypeController {


    @Autowired
    private CoursewareTypeService coursewareTypeService;


    @GetMapping
    public Result<List<CoursewareType>> list(CoursewareType userInfo) {
        QueryWrapper<CoursewareType> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        List<CoursewareType> list = coursewareTypeService.list();
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<CoursewareType> find(@PathVariable("id") Integer id) {

        CoursewareType byId = coursewareTypeService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
    public Result<?> save(@RequestBody CoursewareType userInfo) {

        boolean save = coursewareTypeService.save(userInfo);
        return Result.ok();
    }


    @PostMapping("update")
    public Result<?> update(@RequestBody CoursewareType userInfo) {

        boolean save = coursewareTypeService.updateById(userInfo);
        return Result.ok();
    }

    @PostMapping("removeById/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = coursewareTypeService.removeById(id);
        return Result.ok();
    }

}
