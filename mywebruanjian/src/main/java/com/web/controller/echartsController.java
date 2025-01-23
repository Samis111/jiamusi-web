package com.web.controller;

import com.web.domain.common.Result;
import com.web.service.StudentScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;

@RequestMapping("/echarts")
@RestController
public class echartsController {

    @Autowired
    private StudentScoresService studentScoresService;


    @RequestMapping("/getScores")
    public Result getScores(@RequestParam("paperId") Integer paperId){
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        Integer max =  studentScoresService.getMax(paperId);
      Integer min =  studentScoresService.getMin(paperId);
      Integer avg =  studentScoresService.getAvg(paperId);
      //  全部人数
      Integer count =  studentScoresService.getCount(paperId);
      //  A B 人数

        HashMap<Integer, Integer> integerIntegerHashMap = new HashMap<>();
        integerIntegerHashMap.put(100,90);
        integerIntegerHashMap.put(90,80);
        integerIntegerHashMap.put(80,70);
        integerIntegerHashMap.put(70,60);
        integerIntegerHashMap.put(60,0);
        ArrayList<Integer> integers = new ArrayList<>();
        for (Integer key : integerIntegerHashMap.keySet()) {

            Integer i = integerIntegerHashMap.get(key);
            Integer aAndB = studentScoresService.getAAndB(key, i, paperId);
            integers.add(aAndB);
        }

        stringObjectHashMap.put("max",max);
        stringObjectHashMap.put("min",min);
        stringObjectHashMap.put("avg",avg);
        stringObjectHashMap.put("count",count);
        stringObjectHashMap.put("integers",integers);
        return Result.ok(stringObjectHashMap);
    }


}
