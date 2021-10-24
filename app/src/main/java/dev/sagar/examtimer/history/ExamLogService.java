package dev.sagar.examtimer.history;

import static dev.sagar.examtimer.Constants.PROP_MOCK_SERVICES;

import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import dev.sagar.examtimer.pojo.ExamLog;

public class ExamLogService {
    private static ExamLogService instance = null;

    private ExamLogService(){}

    public static ExamLogService getInstance(Activity activity) {
        if (instance == null) {
            synchronized(ExamLogService.class) {
                Properties properties = new Properties();
                try {
                    InputStream inputStream = activity.getBaseContext().getAssets().open("config.properties");
                    properties.load(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                boolean mockService = Boolean.parseBoolean(properties.getProperty(PROP_MOCK_SERVICES, "false"));
                if( mockService ) {
                    instance = new MockExamLogService();
                }
                else{
                    instance = new ExamLogService();
                }
            }
        }
        return instance;
    }

    public List<ExamLog> getLogList(){
        throw new UnsupportedOperationException("This functionality is not yet supported");
    }

    public ExamLog getExamLog(String id) {
        throw new UnsupportedOperationException("This functionality is not yet supported");
    }

    private static class MockExamLogService extends ExamLogService{
        @Override
        public List<ExamLog> getLogList() {
            List<ExamLog> examLogs = new ArrayList<>(10);
            for(int i=0; i<10; i++){
                ExamLog log = new ExamLog();
                log.setStartDateTime(LocalDateTime.now());
                log.setEndDateTime(LocalDateTime.now().plusHours(1));
                log.setTimeTaken(Duration.ofHours(i).plusMinutes(i).plusSeconds(i*10));
                log.setQuestionsAttempted(100-i);
                examLogs.add(log);
            }

            return examLogs;
        }

        @Override
        public ExamLog getExamLog(String id) {
            int totalQuestions = 40;

            ExamLog log = new ExamLog();
            log.setStartDateTime(LocalDateTime.now());
            log.setEndDateTime(LocalDateTime.now().plusHours(5));
            log.setQuestionsAttempted(totalQuestions);
            log.setId(id);
            log.setTimeTaken(Duration.between(log.getStartDateTime(), log.getEndDateTime()));
            List<ExamLog.QuestionLog> questions = new ArrayList<>();
            Random random = new Random();
            for(int i=0; i<totalQuestions; i++){
                ExamLog.QuestionLog questionLog = new ExamLog.QuestionLog();
                questionLog.setDuration(Duration.ofSeconds(Math.abs(random.nextInt(500))));
                questionLog.setIndex(i);
                questions.add(questionLog);
            }
            log.setQuestionLogList(questions);

            return log;
        }
    }
}
