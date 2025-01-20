package com.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.DTO.Options;
import com.web.domain.DTO.QuestionExercise;
import com.web.domain.ExerciseQuestions;
import com.web.domain.ExerciseType;
import com.web.domain.NewtextPapers;
import com.web.domain.TestPapers;
import com.web.domain.common.Result;
import com.web.service.ExerciseQuestionsService;
import com.web.service.ExerciseTypeService;
import com.web.service.NewtextPapersService;
import com.web.service.TestPapersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("exercise")
public class ExerciseQuestionsController {


    @Autowired
    private ExerciseQuestionsService exerciseQuestionsService;

    @Autowired
    private NewtextPapersService newtextPapersService;

    @Autowired
    private TestPapersService testPapersService;

    @GetMapping("getExerciseQuestions/{id}")
    public Result<List<ExerciseQuestions>> getExerciseQuestions(@PathVariable("id") Integer id) {
        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("question_creator_id", id);
        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);

    }

    @GetMapping("getNewExerciseQuestions/{id}")
    public Result<List<ExerciseQuestions>> getNewExerciseQuestions(@PathVariable("id") Integer id) {
        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("newquestion_id", id);
        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);

    }

    @GetMapping("list")
    public Result<List<ExerciseQuestions>> list(ExerciseQuestions exerciseQuestions) {
        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();

        if (exerciseQuestions.getQuestionContent() != null && !exerciseQuestions.getQuestionContent().equals("")) {
            queryWrapper.like("question_content", exerciseQuestions.getQuestionContent());
        }

        if (exerciseQuestions.getQuestionTypeId() != null && exerciseQuestions.getQuestionTypeId() != 0) {
            queryWrapper.eq("question_type_id", exerciseQuestions.getQuestionTypeId());
        }
        queryWrapper.eq("status", "1");
        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("{id}")
    public Result<ExerciseQuestions> find(@PathVariable("id") Integer id) {

        ExerciseQuestions byId = exerciseQuestionsService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("create")
    public Result<?> save(@RequestBody ExerciseQuestions exerciseQuestions) {

        boolean save = exerciseQuestionsService.save(exerciseQuestions);
        return Result.ok();
    }

    @PostMapping("createQuestions")
    public Result<?> createQuestions(@RequestBody QuestionExercise exerciseQuestions) {
        System.out.println(exerciseQuestions.toString());


        if (exerciseQuestions.getQuestionTypeId() == 3) {

            ExerciseQuestions questions = new ExerciseQuestions();
            questions.setQuestionAnswer(exerciseQuestions.getAnswer());
            questions.setQuestionContent(exerciseQuestions.getContent());
            questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
            questions.setQuestionCount(exerciseQuestions.getScore());
            questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
            questions.setNewquestionId(exerciseQuestions.getNewquestionId());

            boolean save = exerciseQuestionsService.save(questions);
        } else if (exerciseQuestions.getQuestionTypeId() == 1) {

            ExerciseQuestions questions = new ExerciseQuestions();

            questions.setQuestionContent(exerciseQuestions.getContent());
            questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
            questions.setQuestionCount(exerciseQuestions.getScore());
            questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
            List<Options> options = exerciseQuestions.getOptions();
            String s = appendOptions(options, questions.getQuestionAnswer());
            questions.setQuestionAnswer(s);
            questions.setNewquestionId(exerciseQuestions.getNewquestionId());
            boolean save = exerciseQuestionsService.save(questions);

        } else if (exerciseQuestions.getQuestionTypeId() == 4) {

            ExerciseQuestions questions = new ExerciseQuestions();

            questions.setQuestionContent(exerciseQuestions.getContent());
            questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
            questions.setQuestionCount(exerciseQuestions.getScore());


            questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());

            questions.setQuestionAnswer(null);
            questions.setNewquestionId(exerciseQuestions.getNewquestionId());
            boolean save = exerciseQuestionsService.save(questions);

        }

        if (exerciseQuestions.getQuestionCreatorId() != null && exerciseQuestions.getQuestionCreatorId() != 0) {
            NewtextPapers byId = newtextPapersService.getById(exerciseQuestions.getQuestionCreatorId());
            Integer integer = Integer.valueOf(byId.getTotalScore());

            Integer score = exerciseQuestions.getScore();
            Integer i1 = integer + score;
            byId.setTotalScore(i1 + "");
            newtextPapersService.updateById(byId);

        } else if (exerciseQuestions.getNewquestionId() != null && exerciseQuestions.getNewquestionId() != 0) {

            TestPapers byId = testPapersService.getById(exerciseQuestions.getNewquestionId());
            Integer integer = Integer.valueOf(byId.getTotalScore());

            Integer score = exerciseQuestions.getScore();
            Integer i1 = integer + score;
            byId.setTotalScore(i1 + "");
            testPapersService.updateById(byId);
        }


        return Result.ok();
    }

    private String appendOptions(List<Options> list, String answer) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (Options options : list) {
            stringBuilder.append(options.getLabel()).append(",");
        }
        String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
        substring = substring + "]";

        substring = substring + "[" + answer + "]";
        return substring;
    }


    @PutMapping("publish/{id}")
    public Result<?> publish(@PathVariable("id") Integer id) {

        ExerciseQuestions byId = exerciseQuestionsService.getById(id);

        if (byId.getStatus() == 0) {
            byId.setStatus(1);
            exerciseQuestionsService.updateById(byId);
        } else if (byId.getStatus() == 1) {
            byId.setStatus(0);
            exerciseQuestionsService.updateById(byId);
        }

        return Result.ok();
    }

    @PutMapping("update")
    public Result<?> update(@RequestBody ExerciseQuestions userInfo) {

        boolean save = exerciseQuestionsService.updateById(userInfo);
        return Result.ok();
    }

    @RequestMapping("delete/{id}")
    public Result<?> removeById(@PathVariable Integer id) {

        boolean byId = exerciseQuestionsService.removeById(id);
        return Result.ok();
    }

}
