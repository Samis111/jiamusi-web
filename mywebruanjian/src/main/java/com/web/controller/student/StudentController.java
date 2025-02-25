package com.web.controller.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.web.domain.*;
import com.web.domain.DTO.Options;
import com.web.domain.DTO.StudentRepliesAndTopics;
import com.web.domain.DTO.StudentTestPapers;
import com.web.domain.DTO.TestPapersDTO;
import com.web.domain.common.Result;
import com.web.service.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("student")
public class StudentController {

    @Autowired
    private TestPapersService testPapersService;

    @Autowired
    private DiscussionTopicsService discussionTopicsService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private DiscussionRepliesService discussionRepliesService;

    @Autowired
    private ExerciseQuestionsService exerciseQuestionsService;

    @Autowired
    private NewtextPapersService newtextPapersService;

    @Autowired
    private StudentScoresService studentScoresService;

    @GetMapping("/exercise/list")
    public Result<List<NewtextPapers>> exerciseList(@RequestParam("keyword") String keyword) {


//        List<StudentTestPapers> list = testPapersService.joinScores(id);
        List<NewtextPapers> list = newtextPapersService.list();
        return Result.ok(list);
    }

    @GetMapping("/test/list")
    public Result<List<TestPapers>> textList(@RequestParam("keyword") String keyword, @RequestParam("userId") Integer userId) {
        QueryWrapper<TestPapers> queryWrapper = new QueryWrapper<>();
        List<TestPapers> list = testPapersService.list(queryWrapper);



        for (TestPapers testPapers : list) {
            Integer paperId = testPapers.getPaperId();


            QueryWrapper<StudentScores> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("student_id", userId).eq("paper_id", paperId);
           try {
               StudentScores one = studentScoresService.list(queryWrapper1).get(0);
               if (one != null) {
                   testPapers.setNewStatus(1);

               }
           }catch (Exception e){

                   testPapers.setNewStatus(0);
           }

        }



        return Result.ok(list);
    }

    @GetMapping("/interaction/list")
    public Result<List<DiscussionTopics>> interactionList(@RequestParam("keyword") String keyword) {
        QueryWrapper<DiscussionTopics> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("topic_title", keyword);
        List<DiscussionTopics> list = discussionTopicsService.list(queryWrapper);
        for (DiscussionTopics discussionTopics : list) {
            UserInfo userInfoServiceById = userInfoService.getById(discussionTopics.getTopicCreatorId());
            discussionTopics.setCreator(userInfoServiceById.getUsername());
        }
        return Result.ok(list);
    }


    @GetMapping("/interaction/detail/{id}")
    public Result<StudentRepliesAndTopics> detail(@PathVariable("id") String id) {

        DiscussionTopics byId = discussionTopicsService.getById(id);
        UserInfo userInfoServiceById = userInfoService.getById(byId.getTopicCreatorId());
        byId.setCreator(userInfoServiceById.getUsername());
        QueryWrapper<DiscussionReplies> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id", id);
        List<DiscussionReplies> list = discussionRepliesService.list(queryWrapper);
        byId.setNumber(list.size());
        for (DiscussionReplies discussionReplies : list) {
            UserInfo userInfoServiceById1 = userInfoService.getById(discussionReplies.getReplyCreatorId());
            discussionReplies.setUsername(userInfoServiceById1.getUsername());
        }
        StudentRepliesAndTopics studentRepliesAndTopics = new StudentRepliesAndTopics(byId, list);

        return Result.ok(studentRepliesAndTopics);
    }


    @RequestMapping("/interaction/reply")
    public Result<?> reply(@RequestBody DiscussionReplies discussionReplies) {
        boolean save = discussionRepliesService.save(discussionReplies);
        return Result.ok();
    }


    @RequestMapping("/interaction/create")
    public Result<?> create(@RequestBody DiscussionTopics discussionTopics) {
        boolean save = discussionTopicsService.save(discussionTopics);
        return Result.ok();
    }


    @PostMapping("/test/submit")
    public Result<?> submit(@RequestBody TestPapersDTO testPapersDTO) {
        System.out.println(testPapersDTO);
        Integer chengji = 0;

        for (ExerciseQuestions exerciseQuestions : testPapersDTO.getAnswers()) {


            if (exerciseQuestions.getQuestionTypeId() == 3) {

                ExerciseQuestions questions = new ExerciseQuestions();
                questions.setQuestionAnswer(exerciseQuestions.getAnswer());

                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCount(exerciseQuestions.getQuestionCount());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());

                String questionAnswer = exerciseQuestions.getQuestionAnswer();
                String answer = exerciseQuestions.getAnswer();
                if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }

            } else if (exerciseQuestions.getQuestionTypeId() == 1) {

                ExerciseQuestions questions = new ExerciseQuestions();


                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCount(exerciseQuestions.getQuestionCount());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());


                String questionAnswer = getAnswer(exerciseQuestions.getQuestionAnswer());
                String answer = exerciseQuestions.getAnswer();

                 if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }

            } else if (exerciseQuestions.getQuestionTypeId() == 4) {

                ExerciseQuestions questions = new ExerciseQuestions();

                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setQuestionAnswer(exerciseQuestions.getAnswer());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());

                String questionAnswer = exerciseQuestions.getQuestionAnswer();
                String answer = exerciseQuestions.getAnswer();
                if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }
            }

        }


        TestPapers testPapersServiceById = testPapersService.getById(testPapersDTO.getPaperId());

        StudentScores scores = new StudentScores();
//        scores.setTotal();
        scores.setTotalScore(chengji+"");
        scores.setPaperId(testPapersServiceById.getPaperId());
        scores.setPaperName(testPapersServiceById.getPaperName());
        scores.setStudentId(testPapersDTO.getUserId());

        studentScoresService.save(scores);


        return Result.ok(chengji);
    }

    private String getAnswer(String questionAnswer) {
        String[] split = questionAnswer.split("]");
        System.out.println(split);

        String i = split[1].substring(1,split[1].length());

        return i;
    }

    @RequestMapping("/test/detail/{id}")
    public Result<?> testDetail(@PathVariable("id") Integer id) {

        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("newquestion_id", id);

        TestPapers testPapersServiceById = testPapersService.getById(id);

        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);

        testPapersServiceById.setQuestions(list);

        return Result.ok(testPapersServiceById);
    }


    @RequestMapping("/exercise/detail/{id}")
    public Result<?> exerciseDetail(@PathVariable("id") Integer id) {

        QueryWrapper<ExerciseQuestions> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("newquestion_id", id);

        NewtextPapers testPapersServiceById = newtextPapersService.getOne(new QueryWrapper<NewtextPapers>().eq("paper_creator_id",id));

        List<ExerciseQuestions> list = exerciseQuestionsService.list(queryWrapper);

        testPapersServiceById.setQuestions(list);

        return Result.ok(testPapersServiceById);
    }


    @GetMapping("/score/list")
    public Result<List<StudentScores>> scoreList(@RequestParam("userId") Integer userId) {
        QueryWrapper<StudentScores> queryWrapper = new QueryWrapper<>();

//        if (userInfo.getUsername() != null && !userInfo.getUsername().equals("")) {
//            queryWrapper.eq("username", userInfo.getUsername());
//        }

        queryWrapper.eq("student_id", userId);
        List<StudentScores> list = studentScoresService.list(queryWrapper);

        for (StudentScores scores : list) {

            try {
                UserInfo userInfoServiceById = userInfoService.getById(scores.getStudentId());
                TestPapers testPapersServiceById = testPapersService.getById(scores.getPaperId());

                scores.setStudentName(userInfoServiceById.getUsername());
                scores.setPaperName(testPapersServiceById.getPaperName());
                scores.setTotal(testPapersServiceById.getTotalScore());

            } catch (Exception e) {
            }
        }


        return Result.ok(list);
    }


    @PostMapping("/exercise/submit")
    public Result<?> exerciseSubmit(@RequestBody TestPapersDTO testPapersDTO) {
        System.out.println(testPapersDTO);
        Integer chengji = 0;

        for (ExerciseQuestions exerciseQuestions : testPapersDTO.getAnswers()) {


            if (exerciseQuestions.getQuestionTypeId() == 3) {

                ExerciseQuestions questions = new ExerciseQuestions();
                questions.setQuestionAnswer(exerciseQuestions.getAnswer());

                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCount(exerciseQuestions.getQuestionCount());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());

                String questionAnswer = exerciseQuestions.getQuestionAnswer();
                String answer = exerciseQuestions.getAnswer();
                if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }

            } else if (exerciseQuestions.getQuestionTypeId() == 1) {

                ExerciseQuestions questions = new ExerciseQuestions();


                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCount(exerciseQuestions.getQuestionCount());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());


                String questionAnswer = getAnswer(exerciseQuestions.getQuestionAnswer());
                String answer = exerciseQuestions.getAnswer();

                if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }

            } else if (exerciseQuestions.getQuestionTypeId() == 4) {

                ExerciseQuestions questions = new ExerciseQuestions();

                questions.setQuestionTypeId(exerciseQuestions.getQuestionTypeId());
                questions.setQuestionCreatorId(exerciseQuestions.getQuestionCreatorId());
                questions.setQuestionAnswer(exerciseQuestions.getAnswer());
                questions.setNewquestionId(exerciseQuestions.getNewquestionId());

                String questionAnswer = exerciseQuestions.getQuestionAnswer();
                String answer = exerciseQuestions.getAnswer();
                if (questionAnswer.equals(answer)) {
                    Integer questionCount = exerciseQuestions.getQuestionCount();
                    chengji = chengji + questionCount;
                }
            }

        }


        TestPapers testPapersServiceById = testPapersService.getById(testPapersDTO.getPaperId());



        StudentScores scores = new StudentScores();
//        scores.setTotal();
        scores.setTotalScore(chengji+"");
        scores.setPaperId(testPapersServiceById.getPaperId());
        scores.setPaperName(testPapersServiceById.getPaperName());
        scores.setStudentId(testPapersDTO.getUserId());

//        studentScoresService.save(scores);


        return Result.ok(chengji);
    }


    @GetMapping("/get/{paperId}/{userId}")
    public Result get(@PathVariable("userId") Integer userId, @PathVariable("paperId") Integer paperId) {

        StudentScores one = studentScoresService.getOne(new QueryWrapper<StudentScores>().eq("student_id", userId).eq("paper_id", paperId).last("limit 1"));
        return Result.ok(one);

    }
}
