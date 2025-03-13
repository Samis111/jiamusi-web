package com.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.NewtextPapers;
import com.web.domain.common.Result;
import com.web.domain.query.CommonFather;
import com.web.service.NewtextPapersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("NewTextPapers")
public class NewTextPapersController {


    @Autowired
    private NewtextPapersService newtextPapersService;


    @GetMapping("list")
    public Result list(CommonFather commonFather) {

        QueryWrapper<NewtextPapers> queryWrapper = new QueryWrapper<>();

        List<NewtextPapers> list = newtextPapersService.list();
        return Result.ok(list);
    }


    @PostMapping("info/{id}")
    public Result info(@PathVariable("id")Integer id){

        NewtextPapers byId = newtextPapersService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("save")
        public Result save(@RequestBody NewtextPapers newtextPapers){
        boolean save = newtextPapersService.save(newtextPapers);
        return Result.ok();
    }

    @PostMapping("updateById")
        public Result updateById(@RequestBody NewtextPapers newtextPapers){
        boolean save = newtextPapersService.updateById(newtextPapers);
        return Result.ok();
    }

    @PostMapping("del/{id}")
    public Result del(@PathVariable("id")Integer id){
        boolean save = newtextPapersService.removeById(id);
        return Result.ok();
    }

}
