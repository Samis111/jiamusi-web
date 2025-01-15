package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.MultimediaCourseware;
import com.web.domain.PaperQuestionRelation;
import com.web.domain.UserInfo;
import com.web.domain.common.Result;
import com.web.service.MultimediaCoursewareService;
import com.web.service.PaperQuestionRelationService;
import com.web.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courseware")
public class MultimediaController {


    @Autowired
    private MultimediaCoursewareService multimediaCoursewareService;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("list")
    public Result<List<MultimediaCourseware>> list(MultimediaCourseware multimedia) {
        QueryWrapper<MultimediaCourseware> queryWrapper = new QueryWrapper<>();

        if (multimedia.getCoursewareTypeId() != null && multimedia.getCoursewareTypeId()!=0) {
            queryWrapper.eq("courseware_type_id", multimedia.getCoursewareTypeId());
        }

        if (multimedia.getCoursewareName() != null && !multimedia.getCoursewareName().equals("")) {
            queryWrapper.like("courseware_name", multimedia.getCoursewareName());
        }

        List<MultimediaCourseware> list = multimediaCoursewareService.list(queryWrapper);
        for (MultimediaCourseware multimediaCourseware:list){
            UserInfo byId = userInfoService.getById(multimediaCourseware.getCoursewareCreatorId());
            multimediaCourseware.setCreatorName(byId.getUsername());
        }


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

    @DeleteMapping("delete/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = multimediaCoursewareService.removeById(id);
        return Result.ok();
    }

}
