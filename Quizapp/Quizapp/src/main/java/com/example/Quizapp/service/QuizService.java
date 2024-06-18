package com.example.Quizapp.service;

import com.example.Quizapp.Dao.QuestionDao;
import com.example.Quizapp.Dao.QuizDao;
import com.example.Quizapp.model.Question;
import com.example.Quizapp.model.QuestionWrapper;
import com.example.Quizapp.model.Quiz;
import com.example.Quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private QuizDao quizDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> question = quiz.get().getQuestions();
        List<QuestionWrapper> questionForUser = new ArrayList<>();

        for (Question q:question){
            QuestionWrapper qw=new QuestionWrapper(q.getId(),q.getQuestionTitle(),q.getOption1(), q.getOption2(), q.getOption3(),q.getOption4());
            questionForUser.add(qw);
        }
            return new ResponseEntity<>(questionForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> response) {
        Quiz quiz= quizDao.findById(id).get();
        List<Question> questions=quiz.getQuestions();
        int count=0;
        int i=0;
        for(Response r:response){
            if(r.getResponse().equals(questions.get(i).getRightAnswer())){
                count++;
            }
            i++;
        }
        return new ResponseEntity<>(count,HttpStatus.OK);
    }
}
